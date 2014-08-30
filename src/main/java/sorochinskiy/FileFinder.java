package sorochinskiy;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileFinder {

    public static final String FILENAME_REGEX = "(^\\d?\\d)_([0-9a-fA-F]+)_(\\d{4}-\\d{2}-\\d{2})_((?:2[0-3]|[01]?[0-9])[0-5][0-9][0-5][0-9])\\..+";
    public static final String DOT_REGEX = "\\.";
    public static final String GROUP_SPLITER_REGEX = "_";
    public static final String DATE_FORMAT = "yyyy-MM-dd_HHmmss";
    public static final String ERROR_NOT_INITIALISED_MESSAGE = "Finder is empty. There are no files matched. Call createHwAddressToImgEntriesMap function";
    public static final String FILES_NOT_FOUND_MESSAGE = "Finder is empty. There are no files matched in ";
    private Map<String, List<ImgEntry>> imgEntriesMap;
    private String directory;

    public static void main(String[] args) throws IOException {

        String path = null;
        if(args.length > 0){
            path = args[0];
        } else {
            System.out.println("Please, enter path to images folder:");
            Scanner in = new Scanner(System.in);
            path = in.nextLine();
        }

        FileFinder finder = new FileFinder();
        Map<String, List<ImgEntry>> map = finder.createHwAddressToImgEntriesMap(path);
        System.out.println(finder.toString());

    }

    public Map<String, List<ImgEntry>> createHwAddressToImgEntriesMap(String directory) throws IOException {
        this.directory = directory;
        imgEntriesMap = new HashMap<>();
        for(Path filePath : getMachedFiles(directory)) {

            String[] splitedName = filePath.getFileName().toString().split(DOT_REGEX)[0].split(GROUP_SPLITER_REGEX);

            Integer eventCode = Integer.valueOf(splitedName[0]);
            String hwAddress = splitedName[1];
            Calendar calendar = parseCalendar(splitedName[2] + GROUP_SPLITER_REGEX + splitedName[3]);
            String md5Hex = getMd5Hex(filePath);

            ImgEntry entry = new ImgEntry(eventCode, hwAddress, calendar, md5Hex);

            putImgEntry(entry);

        }
        return imgEntriesMap;
    }

    //Task 2
    @Override
    public String toString(){
        if(imgEntriesMap == null)
            return ERROR_NOT_INITIALISED_MESSAGE;
        if(imgEntriesMap.isEmpty())
            return FILES_NOT_FOUND_MESSAGE + directory;
        StringBuilder sb = new StringBuilder();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Map.Entry<String, List<ImgEntry>> entry : imgEntriesMap.entrySet()) {
            sb.append(entry.getKey()).append(" = [\n");
            for (ImgEntry imgEntry : entry.getValue()) {
                sb.append("\t{\n" + "\t\tevCode = ").append(imgEntry.getEventCode())
                        .append(",\n\t\thwAddress = '").append(imgEntry.getHwAddress()).append('\'')
                        .append(",\n\t\ttime = ").append(formatter.format(imgEntry.getTime().getTime()))
                        .append(",\n\t\tmd5Hex = '").append(imgEntry.getMd5Hex()).append('\'')
                        .append("\n\t},\n");
            }
            sb.delete(sb.length()-2, sb.length()-1).append("],\n");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    private Collection<Path> getMachedFiles(final String directory) throws IOException {
        Collection<Path> files = new ArrayList<>();
        Files.walk(Paths.get(directory)).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.getFileName().toString().matches(FILENAME_REGEX)) {
                files.add(filePath);
            }
        });
        return files;
    }

    private void putImgEntry(ImgEntry entry) {
        if(imgEntriesMap.containsKey(entry.getHwAddress())){
            List<ImgEntry> entryList = imgEntriesMap.get(entry.getHwAddress());
            entryList.add(entry);
        } else {
            List<ImgEntry> entryList = new ArrayList<>();
            entryList.add(entry);
            imgEntriesMap.put(entry.getHwAddress(), entryList);
        }
    }

    private String getMd5Hex(Path filePath) {
        String md5Hex = "";

        try(FileInputStream fis = new FileInputStream(filePath.toFile())) {
            md5Hex = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5Hex;
    }

    private static Calendar parseCalendar(String imgDate) {
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(imgDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}

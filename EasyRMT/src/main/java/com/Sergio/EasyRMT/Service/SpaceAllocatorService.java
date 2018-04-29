package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SpaceAllocatorService {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );

    @Autowired
    public SpaceAllocatorService() {
    }

    //TODO JAVADoc

    /**
     *
     * @param file
     * @param path
     * @return
     */
    public boolean filePersist(MultipartFile file, String path){
        /**
         *  Check if a folder exists.
         *  If not exists creates the new directory
         *  then, saves file
         */
        try {
            File folder = new File(path);
            if (!(folder.exists() && folder.isDirectory())) {
                folder.mkdirs();
            }
            /**
             * checks if file exists
             * If exists, save file but with date (yyyyMMdd) before name.
             * else just save file.
             */
            File filepath = new File(path+'/'+file.getOriginalFilename());
            if(filepath.exists()){
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String formatDate = sdf.format(date);
                path.concat('/'+formatDate+file.getOriginalFilename());
            }else {
                path.concat('/'+file.getOriginalFilename());
            }

            byte[] bytes = file.getBytes();
            Path path1 = Paths.get(path);
            Files.write(path1,bytes);
            return true;

        }
        catch (IOException e){
            LOGGER.log(Level.WARNING, "System failed to create and persist file");
            return false;
        }
    }

}

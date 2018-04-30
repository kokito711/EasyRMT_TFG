/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */
package com.Sergio.EasyRMT.Service;

import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
                boolean status =folder.mkdirs();
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
                path = path.concat('/'+formatDate+file.getOriginalFilename());
            }else {
               path = path.concat('/'+file.getOriginalFilename());
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

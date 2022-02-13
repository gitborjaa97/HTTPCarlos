/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuputamadre;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author carlo
 */
public class Hilo implements  Runnable{

    private Socket s;
    public Hilo(Socket s) {
        this.s = s;
    }

    
    @Override
    public void run() {
        try 
        {            
            InputStream in = s.getInputStream();
            OutputStream out = s.getOutputStream();
            
            byte[] recurso =  getHTML("site\\index.html");
//            String html_pagina2 = getHTML("C:\\Users\\carlo\\NetBeansProjects\\tuputamadre\\site\\pagina2.html");
//            String css = getHTML("C:\\Users\\carlo\\NetBeansProjects\\tuputamadre\\site\\main.css");
           // BufferedImage image = getImagesAndIcon(in);
           
           
            String respuesta_index = "HTTP/1.1 200 OK \n\r"+ "Content-Type: text/html \r\n"
                    +"Content-Length: " + recurso.length + "\n\r\n\r";
//            String respuesta_pagina2 = "HTTP/1.1 200 OK \n\r Content-Length: " + html_pagina2.length  + "\n\r\n\r" + html_pagina2 + "\n\r\n\r";
//            String respuesta_css = "HTTP/1.1 200 OK \n\r Content-Length: " +  + css.getBytes().length + "\n\r\n\r" + css + "\n\r\n\r";
            
            int _byte;
            String cabecera;
            boolean datos_obtenidos;
            
            datos_obtenidos = false;
            cabecera = "";
             while((_byte = in.read()) >= 0 && !datos_obtenidos)
             {
                if(_byte == 13)
                    datos_obtenidos = true;
                else if(!datos_obtenidos)
                    cabecera += (char)_byte;
                   
             }
             System.out.println("--->" + cabecera);
            if(cabecera.equals("GET / HTTP/1.1") || cabecera.equals("GET /index.html HTTP/1.1")){
                System.out.println(respuesta_index);
                out.write(respuesta_index.getBytes());
                out.write(recurso);
                out.write("\n\r\n\r".getBytes());
            }
//            else if(cabecera.equals("GET /pagina2.html HTTP/1.1"))
//                out.write(respuesta_pagina2.getBytes());
//            else if(cabecera.equals("GET /main.css HTTP/1.1"))
//                out.write(respuesta_css.getBytes());
         //   else if(cabecera.equals("GET /imagen.png HTTP/1.1"))
               // ImageIO.write(image, "png", new File("C:\\Users\\carlo\\NetBeansProjects\\tuputamadre\\site\\imagen.png"));
        } catch (IOException ex) {}
    }

    private BufferedImage getImagesAndIcon(InputStream in) {
         BufferedImage image = null;
        try {
            byte[] imageAr = new byte[62100];
            in.read(imageAr);
            image = ImageIO.read(new ByteArrayInputStream(imageAr));
        } catch (IOException ex) {}
         return image;
    }
    
     private static byte [] getHTML(String siteindexhtml) 
    {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(siteindexhtml));
        } catch (IOException ex) {
            Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bytes;
    }
    
}

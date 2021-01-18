package sample;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.Math.*;

public class VectorQuatization {
    ArrayList<vector> codebook,allVectors;
    int n,m,numberOfVectors;
    int width,length;
    int[][]pixel;
    File decodedPath,imagePath;
    ArrayList<Integer> encoded;
    ArrayList<ArrayList<vector>> nearset;
    BufferedImage image;
    VectorQuatization(){
        image = null;
        nearset = new ArrayList<>();
        encoded = new ArrayList<>();
        decodedPath = imagePath = null;
        pixel = null;
        nearset = new ArrayList<>();
        codebook = new ArrayList<>();
        allVectors = new ArrayList<>();
    }
    void buildAllVectors(){
        for (int i = 0; i < width; i+=n) {
            for (int j = 0; j < length; j+=m) {
                vector curVector  = new vector(n,m);
                for (int x = 0; x < n; x++) {
                    for (int y = 0; y < m; y++) {
                        if (i + x >= width || j + y >= length)
                            continue;
                        curVector.setElement(x,y,pixel[i+x][j+y]);
                    }
                }
                allVectors.add(curVector);
            }
        }
    }
    void spilt() {
        ArrayList<vector> newCodeBook = new ArrayList<>();
        for (vector code : codebook) {
            vector b1 = new vector(n, m), b2 = new vector(n, m);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    b1.data[i][j] = (int) ceil(code.data[i][j] - 1);
                    b2.data[i][j] = (int) floor(code.data[i][j] + 1);
                }
            }
            newCodeBook.add(b1);
            newCodeBook.add(b2);
        }
        codebook = newCodeBook;
    }
    vector calculateAverage(ArrayList<vector> c) {

        int[][] data = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] = 0;
                for (vector v : c) {
                    data[i][j] += v.getElement(i, j);
                }
                data[i][j] /= c.size();
            }
        }
        return new vector(data, n, m);
    }
    ArrayList<ArrayList<vector>> getNearset(){
        ArrayList<ArrayList<vector>> nearset = new ArrayList<>();
        for (int i = 0; i < codebook.size(); i++) {
            nearset.add(new ArrayList<vector>());
        }
        for (int i = 0; i < allVectors.size(); i++) {
            int minimumDistance = Integer.MAX_VALUE;
            int nearsetIndex = 0;
            for (int j = 0; j < codebook.size(); j++) {
                int distance = codebook.get(j).getDistance(allVectors.get(i));
                if(distance<minimumDistance){
                    minimumDistance = distance;
                    nearsetIndex = j;
                }
            }
            nearset.get(nearsetIndex).add(allVectors.get(i));
        }
        return nearset;
    }
    void buildCodeBook(){
        codebook.add(calculateAverage(allVectors));

        while(numberOfVectors>codebook.size()){
            spilt();
            ArrayList<ArrayList<vector>> nearsetVectors = getNearset();
            for (int i = 0; i < codebook.size(); i++) {
                if(nearsetVectors.get(i).size()>0)
                    codebook.set(i,calculateAverage(nearsetVectors.get(i)));
            }
        }
        ArrayList<vector> previousCodeBook = codebook;
        while(true){
            ArrayList<ArrayList<vector>> nearsetVectors = getNearset();
            for (int i = 0; i < codebook.size(); i++) {
                if(nearsetVectors.get(i).size()>0)
                codebook.set(i,calculateAverage(nearsetVectors.get(i)));
            }
            if(codebook.equals(previousCodeBook)){
                break;
            }
            previousCodeBook = codebook;
        }
    }
    
    void encode(){
        for (int i = 0; i < allVectors.size(); i++) {
            int minimumDistance = Integer.MAX_VALUE;
            int nearsetIndex = 0;
            for (int j = 0; j < codebook.size(); j++) {
                int distance = codebook.get(j).getDistance(allVectors.get(i));
                if(distance<minimumDistance){
                    minimumDistance = distance;
                    nearsetIndex = j;
                }
            }
            encoded.add(nearsetIndex);
        }
    }
    
    void getPixels(){
        width = image.getWidth();
        length = image.getHeight();
        System.out.println(width+" "+length);
        pixel = new int[width][length];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                try {
                    int rgb = image.getRGB(i, j);
                    int gray = (rgb >> 16) & 0xff;
                    pixel[i][j] = gray;
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println(e.getMessage());
                    System.out.println(i+ " "+j);
                    return;
                }
            }
        }
    }
    BufferedImage decode() {
        int[][] decodedPixels = new int[width][length];
        int i = 0, j = 0, cnt = 0;
        for (int k = 0; k < encoded.size(); k++) {
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < m; y++) {
                    if (i + x >= width || j + y >= length)
                        continue;
//                    decodedPixels[i+x][j+y] = codebook.get(encoded.get(k)).getElement(x, y);
                    decodedPixels[i + x][j + y] = codebook.get(encoded.get(k)).getElement(x, y);
                }
            }
            cnt++;
            if(cnt%((length+m-1)/m)>0){
                j+=m;
            }
            else{
                i+=n;
                j=0;
            }
        }
        BufferedImage decodedImage = new BufferedImage(width,length,BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                decodedImage.setRGB(x, y, decodedPixels[x][y]);
                decodedImage.setRGB(x, y, (decodedPixels[x][y] << 16) | (decodedPixels[x][y] << 8) | (decodedPixels[x][y]));

            }
        }
        return decodedImage;
    }
    void readImage(){
        try {
            System.out.println(imagePath);
            image = ImageIO.read(imagePath);
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
    void writeImage(BufferedImage image){
        try {
            File file = new File(imagePath.getParent()+"\\decoded.png");
            ImageIO.write(image,"jpg",file);
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
    void getOutputDecode(){
        writeImage(decode());
    }

    void readInput(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter length of vector: ");
        this.n = in.nextInt();
        System.out.println("Enter width of vector: ");
        this.m = in.nextInt();
        System.out.println("Enter number of vectors: ");
        this.numberOfVectors = in.nextInt();
        System.out.println("Choose path of image to encode: ");
        in.nextLine();
        String s = in.nextLine();
        imagePath = new File(s);
        readImage();
        decodedPath = imagePath;
        getPixels();
        buildAllVectors();
        buildCodeBook();
        encode();
        getOutputDecode();
        /*
C:\Users\Mo\Desktop\WhatsApp Image 2019-09-16 at 2.33.27 PM.jpeg
         */
    }


}

/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class. 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;

class IMP implements MouseListener{
   JFrame frame;
   JPanel mp;
   JButton start;
   JScrollPane scroll;
   JMenuItem openItem, exitItem, resetItem;
   Toolkit toolkit;
   File pic;
   ImageIcon img;
   int colorX, colorY;
   int [] pixels;
   int [] results;
   //Instance Fields you will be using below
   
   //This will be your height and width of your 2d array
   int height=0, width=0;
   
   //your 2D array of pixels
    int picture[][];

    /* 
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown 
     * menu is how you will open an image to manipulate. 
     */
   IMP()
   {
      toolkit = Toolkit.getDefaultToolkit();
      frame = new JFrame("Image Processing Software by Hunter");
      JMenuBar bar = new JMenuBar();
      JMenu file = new JMenu("File");
      JMenu functions = getFunctions();
      frame.addWindowListener(new WindowAdapter(){
            @Override
              public void windowClosing(WindowEvent ev){quit();}
            });
      openItem = new JMenuItem("Open");
      openItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ handleOpen(); }
           });
      resetItem = new JMenuItem("Reset");
      resetItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ reset(); }
           });     
      exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ quit(); }
           });
      file.add(openItem);
      file.add(resetItem);
      file.add(exitItem);
      bar.add(file);
      bar.add(functions);
      frame.setSize(600, 600);
      mp = new JPanel();
      mp.setBackground(new Color(0, 0, 0));
      scroll = new JScrollPane(mp);
      frame.getContentPane().add(scroll, BorderLayout.CENTER);
      JPanel butPanel = new JPanel();
      butPanel.setBackground(Color.black);
      start = new JButton("start");
      start.setEnabled(false);
      start.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ fun1(); }
           });
      butPanel.add(start);
      frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
      frame.setJMenuBar(bar);
      frame.setVisible(true);      
   }
   
   /* 
    * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods 
    * for handling the choice, fun1, fun2, fun3, fun4, etc. etc. 
    */
   
  private JMenu getFunctions()
  {
     JMenu menu = new JMenu("Functions");
     
     JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
     JMenuItem secondItem = new JMenuItem("Rotate");
     JMenuItem thirdItem = new JMenuItem("Greyscale");
     JMenuItem fourthItem = new JMenuItem("Blur");
     JMenuItem fifthItem = new JMenuItem("Edge");
     firstItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){fun1();}
           });



      secondItem.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){rotate();}
      });



      thirdItem.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){greyScale();}
      });

      fourthItem.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){blur();}
      });

      fifthItem.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){edge();}
      });

      menu.add(firstItem);
      menu.add(secondItem);
      menu.add(thirdItem);
      menu.add(fourthItem);
      menu.add(fifthItem);
     
      return menu;

  }
  
  /*
   * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame. 
   * You don't need to worry about this method. 
   */
    private void handleOpen()
  {  
     img = new ImageIcon();
     JFileChooser chooser = new JFileChooser();
      Preferences pref = Preferences.userNodeForPackage(IMP.class);
      String path = pref.get("DEFAULT_PATH", "");

      chooser.setCurrentDirectory(new File(path));
     int option = chooser.showOpenDialog(frame);
     
     if(option == JFileChooser.APPROVE_OPTION) {
        pic = chooser.getSelectedFile();
        pref.put("DEFAULT_PATH", pic.getAbsolutePath());
       img = new ImageIcon(pic.getPath());
      }
     width = img.getIconWidth();
     height = img.getIconHeight(); 
     
     JLabel label = new JLabel(img);
     label.addMouseListener(this);
     pixels = new int[width*height];
     
     results = new int[width*height];
  
          
     Image image = img.getImage();
        
     PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width );
     try{
         pg.grabPixels();
     }catch(InterruptedException e)
       {
          System.err.println("Interrupted waiting for pixels");
          return;
       }
     for(int i = 0; i<width*height; i++)
        results[i] = pixels[i];  
     turnTwoDimensional();
     mp.removeAll();
     mp.add(label);
     
     mp.revalidate();
  }
  
  /*
   * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
   * So this method changes the one dimensional array to a two-dimensional. 
   */
  private void turnTwoDimensional()
  {
     picture = new int[height][width];
     for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          picture[i][j] = pixels[i*width+j];
      
     
  }
  /*
   *  This method takes the picture back to the original picture
   */
  private void reset()
  {
        for(int i = 0; i<width*height; i++)
             pixels[i] = results[i]; 
       Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
     
       mp.revalidate(); 
    }
  /*
   * This method is called to redraw the screen with the new image. 
   */
  private void resetPicture()
  {
       for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          pixels[i*width+j] = picture[i][j];
      Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
     
       mp.revalidate();
       mp.updateUI();
   
    }
    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
  private int [] getPixelArray(int pixel)
  {
      int temp[] = new int[4];
      temp[0] = (pixel >> 24) & 0xff;
      temp[1]   = (pixel >> 16) & 0xff;
      temp[2] = (pixel >>  8) & 0xff;
      temp[3]  = (pixel      ) & 0xff;
      return temp;
      
    }
    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer. 
     */
  private int getPixels(int rgb[])
  {
         int alpha = 0;
         int rgba = (rgb[0] << 24) | (rgb[1] <<16) | (rgb[2] << 8) | rgb[3];
        return rgba;
  }
  
  public void getValue()
  {
      int pix = picture[colorY][colorX];
      int temp[] = getPixelArray(pix);
      System.out.println("Color value " + temp[0] + " " + temp[1] + " "+ temp[2] + " " + temp[3]);
    }
  
  /**************************************************************************************************
   * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is 
   * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will 
   * have a 2D array called picture that is holding each pixel from your picture. 
   *************************************************************************************************/
   /*
    * Example function that just removes all red values from the picture. 
    * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array 
    * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
    * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B. 
    * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
    * integer value so you can give it back to the program and display the new picture. 
    */
  private void fun1()
  {
    for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
       {   
          int rgbArray[] = new int[4];
         
          //get three ints for R, G and B
          rgbArray = getPixelArray(picture[i][j]);
         
        
           rgbArray[1] = 0;
           //take three ints for R, G, B and put them back into a single int
           picture[i][j] = getPixels(rgbArray);
        } 
     resetPicture();
  }
    private void edge() {
        int [] dims = findSize(picture);
        int tempPicture[][] = new int[dims[0]][dims[1]];
        int[][] mask2 = {{-1, -1, -1, -1, -1}, {-1, 0, 0, 0, -1}, {-1, 0, 16, 0, -1}, {-1, 0, 0, 0, -1}, {-1, -1, -1, -1, -1}};
        int[][] mask1 = {{-1, -1, -1,}, {-1, 8, -1},  {-1, -1, -1,}};
        greyScale();
       // blur();
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++) {
                if (i > 2 && j > 2 && i < (dims[0] - 2) && j < (dims[1] - 2)) {
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++) {
                            picture[i + (x - 2)][j + (y - 2)] *= mask2[x][y];
                        }
                    }
                }
            }
        }
        resetPicture();
    }
    private void blur()
    {
        int [] dims = findSize(picture);
        int tempPicture[][] = new int[dims[0]][dims[1]];
        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                if(i > 1 && j > 1 && i < (dims[0]-1)&& j < (dims[1]-1)) {
                    int avgRed = 0, avgGreen = 0, avgBlue=0, idk;
                    int rgbArray[] = new int[4];
                    for(int x=i-1; x<=i+1; x++) {
                        for (int y = j-1; y <= j+1; y++) {
                            rgbArray = getPixelArray(picture[x][y]);
                            avgRed += rgbArray[1];
                            avgBlue += rgbArray[2];
                            avgGreen += rgbArray[3];
                        }
                    }
                    avgRed /= 9;
                    avgBlue /= 9;
                    avgGreen /= 9;
                    int i1 = getPixelArray(picture[i][j])[0];
                    int[] output = {255, avgRed, avgBlue, avgGreen};
                    tempPicture[i][j] = getPixels(output);
                }else{
                    tempPicture[i][j] = picture[i][j];
                }
            }
        }
        picture = tempPicture;
        resetPicture();

    }
    private void greyScale()
    {
        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
            {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

               int red = rgbArray[1];
               int green = rgbArray[2];
               int blue = rgbArray[3];
               double lum = (0.2126 * red + 0.7152 * green + 0.0722 * blue);

                rgbArray[1] = (int)lum;
                rgbArray[2] = (int)lum;
                rgbArray[3] = (int)lum;
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    private void rotate()
    {
       int [] dims = findSize(picture);
       int tempPicture[][] = new int[dims[1]][dims[0]];
        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                tempPicture[j][i] = picture[i][j];
            }
        }
        picture = tempPicture;
       int x = width;
        int y = height;
        width = y;
        height = x;
        resetPicture();

    }

    int[] findSize(int[][] inputArr){

        int height = inputArr.length;
        int width = inputArr[0].length;
        int [] output = {height, width};
        return output;
    }

  
  
  private void quit()
  {  
     System.exit(0);
  }

    @Override
   public void mouseEntered(MouseEvent m){}
    @Override
   public void mouseExited(MouseEvent m){}
    @Override
   public void mouseClicked(MouseEvent m){
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }
    @Override
   public void mousePressed(MouseEvent m){}
    @Override
   public void mouseReleased(MouseEvent m){}
   
   public static void main(String [] args)
   {
      IMP imp = new IMP();
   }
 
}
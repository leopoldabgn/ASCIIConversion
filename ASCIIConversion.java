package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ASCIIConversion
{

	public static void main(String[] args)
	{
		String url;
		int size = 30, space = 0;
		boolean x2 = true;
		if(args.length == 0)
		{
			showHelp();
			System.exit(1);
			return;
		}
		else
		{
			url = args[0];
			if(!(new File(url).exists()))
			{
				System.out.println("This file doesn't exist.");
				showHelp();
				System.exit(2);
				return;
			}
		}

		if(args.length > 1)
		{
			try
			{
				size = Integer.parseInt(args[1]);
			}
			catch(Exception e)
			{
				System.out.println("Wrong size !");
				showHelp();
				System.exit(3);
				return;
			}

			if(size <= 0)
			{
				System.out.println("Size must be greater than 0 !");
				showHelp();
				System.exit(4);
				return;
			}

			if(args.length > 2) {
				try {
					space = Integer.parseInt(args[2]);
					if(space < 0)
						throw new Exception();
				} catch(Exception e) {
					System.out.println("Space must be greater or equals to 0 !");
					showHelp();
					System.exit(5);
					return;
				}
				
			}

			if(args.length > 3)
				x2 = false;
		}

		BufferedImage img = resize(loadImg(url), size, size);

		if(img == null)
			return;

		drawTab(getASCIITab(getGrayTab(img), img.getWidth(), img.getHeight()), space, x2);
	}

	public static int[] getNewDim(BufferedImage pic, int w_max, int h_max)
	{
		if(pic == null)
			return null;
		int w, h;
		if(pic.getWidth(null) > w_max)
			w = w_max;
		else
			w = pic.getWidth(null);

		h = pic.getHeight(null);
		float coeff = (float)w / (float)pic.getWidth(null);
		h *= coeff;

		if(h > h_max)
		{
			coeff = (float)h_max / (float)h;
			h = h_max;
			w *= coeff;
		}
		return new int[] {w, h};
	}

    public static BufferedImage resize(BufferedImage inputImg, int w, int h)
    {
    	if(inputImg == null)
    		return null;
    	int[] dim = getNewDim(inputImg, w, h);
    	w = dim[0];
    	h = dim[1];
        BufferedImage outputImage = new BufferedImage(w,
                h, inputImg.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImg, 0, 0, w, h, null);
        g2d.dispose();

        return outputImage;
    }

	public static BufferedImage loadImg(String imgPath)
	{
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return img;
	}

	public static int[][] getGrayTab(BufferedImage img)
	{
		if(img == null)
			return null;
		int pixel, red, green, blue;
		int[][] grayTab = new int[img.getWidth()][img.getHeight()];

		for(int j=0;j<img.getWidth();j++)
		{
			for(int i=0;i<img.getHeight();i++)
			{
				pixel = img.getRGB(j, i);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = pixel & 0xff;
				grayTab[j][i] = (red+green+blue)/3;
			}
		}

		return grayTab;
	}

	public static void drawTab(char[][] tab, int space, boolean x2)
	{
		for(int j=0;j<tab[0].length;j++)	// On affiche le tableau dans le
		{									// bon sens...
			if(space > 0)
				System.out.print(" ".repeat(space));
			for(int i=0;i<tab.length;i++)
			{
				System.out.print(tab[i][j]+"");
				if(x2)
					System.out.print(tab[i][j]+"");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}

	public static char getASCIIByColor(int color)
	{
		String ASCII = "@%#*+=-:.";
		//String ASCII = "@$?§%#*+=-:.";
		int index = 0, coeff = 255/ASCII.length(), k=coeff;
		for(int i=0;i<ASCII.length();i++)
		{
			if(color < k)
			{
				index = i;
				break;
			}
			else if(i == ASCII.length()-1)
			{
				return ASCII.charAt(i);
			}
			k+=coeff;
		}

		return ASCII.charAt(index);
	}

	public static char[][] getASCIITab(int[][] matrix, int c, int l)
	{
		char[][] tab = new char[c][l];
		//c = matrix.length/c;
		//l = matrix[0].length/l;

		for(int j=0;j<c;j++)
		{
			for(int i=0;i<l;i++)
			{
				tab[j][i] = getASCIIByColor(matrix[j][i]);
			}
		}

		return tab;
	}

	public static int[][] getMatrixOfImage(BufferedImage img)
	{
		int w = img.getWidth();
		int h = img.getHeight();
		int[][] matrix = new int[w][h];
		for(int j=0;j<w;j++)
			for(int i=0;i<h;i++)
				matrix[j][i] = img.getRGB(j, i);

		return matrix;
	}

	public static void showHelp()
	{
		System.out.println("Help :");
		System.out.println("java ASCIIConversion.java <image> [size] [space] [x2]");
		System.out.println("-> [size] must be greater than 0");
		System.out.println("-> [space] must be greater or equals to 0");
	}

}

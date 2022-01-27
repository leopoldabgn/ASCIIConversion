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
		int size = 30;
		boolean x2 = true;
		if(args.length == 0)
			url = "photo-identite.jpg";
		else
		{
			url = args[0];
			if(!(new File(url).exists()))
			{
				System.out.println("Le fichier précisé n'existe pas.");
				return;
			}
		}

		if(args.length > 1)
		{
			try
			{
				size = Integer.parseInt(args[1]);
				if(size <= 0)
					size = 30;
			}catch(Exception e){}

			if(args.length > 2)
				x2 = false;
		}



		BufferedImage img = resize(loadImg(url), size, size);

		if(img == null)
			return;

		/*
		int[][] grayTab = getGrayTab(img);

		for(int j=0;j<grayTab.length;j++)
		{
			for(int i=0;i<grayTab[0].length;i++)
				System.out.print(grayTab[j][i]+" ");
			System.out.println();
		}*/

		drawTab(getASCIITab(getGrayTab(img), img.getWidth(), img.getHeight()), x2);
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

	public static void drawTab(char[][] tab, boolean x2)
	{
		for(int j=0;j<tab[0].length;j++)	// On affiche le tableau dans le
		{									// bon sens...
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

}

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import java.awt.Image;


public class GifUtil
{
	
	
	public static void main(String[] args) throws Exception 
	{
		 
		//这个测试代码将./gif目录下的文件x.gif都生成为x_o.gif
		File d = new File("./gifs");
		String targetSuffix = "_o";
		double ratio = 2.0;
		for(File f : d.listFiles())
		{
			if(f.isDirectory())
			{
				continue;
			}
			String fName = f.getName();
			if(fName.toUpperCase().endsWith(".GIF"))
			{
				int index = fName.lastIndexOf(".");
				String mainName = fName.substring(0,index);
				if(!mainName.endsWith(targetSuffix))
				{
					String targetName = d.getAbsolutePath() + File.separator + mainName + targetSuffix + ".GIF";
					resizeByRatio(f.getAbsolutePath(), targetName,ratio,ratio);
					System.out.println("生成文件：" + targetName);
				}
			}
		}
		
	}
	
	

	
	/**
	 * 将srcFile中的gif文件按照宽度比wRation,高度比hRatio缩放之后保存到dstFile中.
	 * 支持透明图缩放,支持动画缩放.
	 */
	static public void resizeByRatio(String srcFile,String dstFile,double wRatio,double hRatio) 
		throws IOException,AWTException
	{
		boolean isAnimated =  isAnimateGif(srcFile);
		if(isAnimated)
		{
			resizeAnimatedImageByDecoder(srcFile,dstFile,wRatio,hRatio);
		}
		else
		{
			resizeNotAnimatedGif(srcFile,dstFile,wRatio,hRatio);
		}
		
	}
	
	/**
	 * 缩放非动画的gif
	 * @param srcFile
	 * @param dstFile
	 * @param wRatio
	 * @param hRatio
	 * @throws IOException
	 * @throws AWTException
	 */
	static public void resizeNotAnimatedGif(String srcFile,String dstFile,double wRatio,double hRatio)
					throws IOException,AWTException
	{
		BufferedImage image = ImageIO.read(new File(srcFile));
		GIFEncoder encoder = new GIFEncoder(resize(image,wRatio,hRatio));
		encoder.Write(new FileOutputStream(dstFile));
	}
	/**
	 * 伸缩图片,保证透明图片正常伸缩
	 * @param srcImage
	 * @param wRatio
	 * @param hRatio
	 * @return
	 */
	static public BufferedImage resize(BufferedImage srcImage,double wRatio,double hRatio)
	{
		
		BufferedImage dstImage = null;   
	    AffineTransform transform = AffineTransform.getScaleInstance(wRatio,hRatio);// 返回表示缩放变换的变换
	    /**
	     * 注意,这个地方伸缩图片,因为需要使用AffineTransformOp.TYPE_NEAREST_NEIGHBOR,这样放大时不会导致颜色增加导致最后超过256色而无法保存为gif
	     */
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);   
	    dstImage = op.filter(srcImage, null);
	    return dstImage;
	}
	
	
	
	/**
	 * 判断一个gif文件是否是动画gif,现在这个处理方式不是很理想
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	static public boolean isAnimateGif(String fileName) throws IOException
	{
		Iterator imageReaders = ImageIO.getImageReadersBySuffix("GIF");
		if (!imageReaders.hasNext()) {
			throw new IOException("no ImageReaders for GIF");
		}
		File file = new File(fileName);
		if(!file.exists())
		{
			throw new IOException("file " + fileName + " is not exists");
		}
		ImageReader imageReader = (ImageReader) imageReaders.next();
		imageReader.setInput(ImageIO.createImageInputStream(file));
		int i = 0;
		while(i < 2)
		{
			try{
				imageReader.read(i);
			}
			catch(IndexOutOfBoundsException ex)
			{
				break;
			}
			++ i;
		}
		imageReader.abort();
		return i >= 2;
	}
	

	/**
	 * 从srcFile中读取gif动画文件，伸缩之后，再保存到dstFile中
	 * @param srcFile  源文件
	 * @param dstFile 目标文件
	 * @param wRatio 宽度伸缩比，1.0便是不变，0.5 宽度表示缩小到一半
	 * @param hRatio 高度伸缩比
	 * @throws IOException 文件读取异常
	 * @throws AWTException gif解析异常
	 */
	static public void resizeAnimatedImageByDecoder(String srcFile,String dstFile, double wRatio,double hRatio) 
		throws IOException,AWTException
	{
		GifDecoder decoder = new GifDecoder();
		decoder.read(srcFile);
		//看有几帧动画
		int count = decoder.getFrameCount();
		AnimatedGifEncoder e = new AnimatedGifEncoder();
		e.start(dstFile);
		//设置动画是否循环播放
		e.setRepeat(decoder.getLoopCount());
		if(decoder.isTransparency())
		{
			e.setTransparent(decoder.lastTransparencyColor);
		}
		
		
		for(int i= 0; i < count; ++ i)
		{
			//设置帧和帧之间延迟
			e.setDelay( decoder.getDelay(i));
			//将缩放过的图片加入当前帧
			e.addFrame(resize(decoder.getFrame(i),wRatio,hRatio));
		}
		e.finish();
	}
	
	



}

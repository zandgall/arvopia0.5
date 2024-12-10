package com.zandgall.arvopia;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.text.html.HTMLDocument.Iterator;

public class Recorder {

	Handler game;
	Robot rt;
	ArrayList<BufferedImage> images;
	public boolean recording;

	public Recorder(Handler game) {
		
		images = new ArrayList<BufferedImage>();
		
		this.game = game;
		try {
			rt = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rt = null;
		}
	}

	public static int screenWidth = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth();

	public static int screenHeight = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getHeight();
	
	public void record() {
		recording = true;
		images.add(rt.createScreenCapture(new Rectangle(screenWidth, screenHeight)));
	}

	public void stop() {
		
		recording = false;
		
		System.out.println("Done");
		
		ImageOutputStream output;
		
		try {
			output = new FileImageOutputStream(new File("C:\\Arvopia\\Recordings\\Recording"+System.currentTimeMillis()+".gif"));
			GifSequenceWriter writer = new GifSequenceWriter(output, images.get(0).getType(), 1, false);
			
			for(BufferedImage b: images) {
				writer.writeToSequence(b);
			}
			
			writer.close();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		
	}

}

// Thanks to Elliot.kroo for this
// http://elliot.kroo.net/software/java/GifSequenceWriter/GifSequenceWriter.java
class GifSequenceWriter {
	protected ImageWriter gifWriter;
	protected ImageWriteParam imageWriteParam;
	protected IIOMetadata imageMetaData;

	/**
	 * Creates a new GifSequenceWriter
	 * 
	 * @param outputStream
	 *            the ImageOutputStream to be written to
	 * @param imageType
	 *            one of the imageTypes specified in BufferedImage
	 * @param timeBetweenFramesMS
	 *            the time between frames in miliseconds
	 * @param loopContinuously
	 *            wether the gif should loop repeatedly
	 * @throws IIOException
	 *             if no gif ImageWriters are found
	 *
	 * @author Elliot Kroo (elliot[at]kroo[dot]net)
	 */
	public GifSequenceWriter(ImageOutputStream outputStream, int imageType, int timeBetweenFramesMS,
			boolean loopContinuously) throws IIOException, IOException {
		// my method to create a writer
		gifWriter = getWriter();
		imageWriteParam = gifWriter.getDefaultWriteParam();
		ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);

		imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);

		String metaFormatName = imageMetaData.getNativeMetadataFormatName();

		IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);

		IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");

		graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
		graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(timeBetweenFramesMS / 10));
		graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

		IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
		commentsNode.setAttribute("CommentExtension", "Created by MAH");

		IIOMetadataNode appEntensionsNode = getNode(root, "ApplicationExtensions");

		IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");

		child.setAttribute("applicationID", "NETSCAPE");
		child.setAttribute("authenticationCode", "2.0");

		int loop = loopContinuously ? 0 : 1;

		child.setUserObject(new byte[] { 0x1, (byte) (loop & 0xFF), (byte) ((loop >> 8) & 0xFF) });
		appEntensionsNode.appendChild(child);

		imageMetaData.setFromTree(metaFormatName, root);

		gifWriter.setOutput(outputStream);

		gifWriter.prepareWriteSequence(null);
	}

	public void writeToSequence(RenderedImage img) throws IOException {
		gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
	}

	/**
	 * Close this GifSequenceWriter object. This does not close the underlying
	 * stream, just finishes off the GIF.
	 */
	public void close() throws IOException {
		gifWriter.endWriteSequence();
	}

	/**
	 * Returns the first available GIF ImageWriter using
	 * ImageIO.getImageWritersBySuffix("gif").
	 * 
	 * @return a GIF ImageWriter object
	 * @throws IIOException
	 *             if no GIF image writers are returned
	 */
	private static ImageWriter getWriter() throws IIOException {
		java.util.Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
		if (!iter.hasNext()) {
			throw new IIOException("No GIF Image Writers Exist");
		} else {
			return iter.next();
		}
	}

	/**
	   * Returns an existing child node, or creates and returns a new child node (if 
	   * the requested node does not exist).
	   * 
	   * @param rootNode the <tt>IIOMetadataNode</tt> to search for the child node.
	   * @param nodeName the name of the child node.
	   * 
	   * @return the child node, if found or a new node created with the given name.
	   */
	  private static IIOMetadataNode getNode(
	      IIOMetadataNode rootNode,
	      String nodeName) {
	    int nNodes = rootNode.getLength();
	    for (int i = 0; i < nNodes; i++) {
	      if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName)
	          == 0) {
	        return((IIOMetadataNode) rootNode.item(i));
	      }
	    }
	    IIOMetadataNode node = new IIOMetadataNode(nodeName);
	    rootNode.appendChild(node);
	    return(node);
	  }
}
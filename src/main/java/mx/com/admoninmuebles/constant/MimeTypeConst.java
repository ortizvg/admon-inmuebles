package mx.com.admoninmuebles.constant;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.util.MimeTypeUtils;

public final class MimeTypeConst {
	
	private MimeTypeConst() throws IllegalAccessException {
		throw new IllegalAccessException();
	}
	
	public static final String IMAGE_JPG_VALUE = "image/jpg";
	
	public static final Collection<String> IMAGE_MIME_TYPES = new ArrayList<String>()
	{
		private static final long serialVersionUID = 1L;

		{
	        add( MimeTypeUtils.IMAGE_GIF_VALUE );
	        add( MimeTypeUtils.IMAGE_JPEG_VALUE );
	        add( MimeTypeUtils.IMAGE_PNG_VALUE );
	        add( IMAGE_JPG_VALUE );
	    };
	};
}

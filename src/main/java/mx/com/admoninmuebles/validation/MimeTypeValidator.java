package mx.com.admoninmuebles.validation;

import mx.com.admoninmuebles.constant.MimeTypeConst;

public final class MimeTypeValidator {
	
	public static boolean isImage(final String mimeType) {
		return MimeTypeConst.IMAGE_MIME_TYPES.parallelStream()
                     .anyMatch( mimiTypeImage -> mimiTypeImage.equalsIgnoreCase( mimeType ) );
	}

}

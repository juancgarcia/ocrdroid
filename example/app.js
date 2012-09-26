// This is a test harness for your module
// You should do something interesting in this harness
// to test out the module and to provide instructions
// to users on how to use it by example.

// open a single window
var win = Ti.UI.createWindow({
	title : 'Tesseract OCR Sample',
	backgroundColor : 'white'
});
var label = Ti.UI.createLabel();
win.add(label);
win.open();

// TODO: write your module tests here
var ocrdroid = require('com.dastardlylabs.ti.ocrdroid');
Ti.API.info("module is => " + ocrdroid);

var ocrText = Ti.UI.createLabel({
	color : '#000000',
	//label using localization-ready strings from <app dir>/i18n/en/strings.xml
	//text : String.format(L('welcome'), 'Titanium'),
	text : '',
	height : 'auto',
	width : 'auto'
});
win.add(ocrText);

if (Ti.Platform.name == "android") {

	var cameraButton = Ti.UI.createButton({
		title : 'launch camera'
	});
	win.add(cameraButton);

	var getTextInImage = function(_TiFilePath){		
		var nativeFilePath = Ti.Filesystem.getFile(_TiFilePath).nativePath;
		
		var foundText = tesstwo.ocr( {image:nativeFilePath,lang:'eng'} );
		ocrText.setText(foundText);
	};


	var cameraConfig = {
		saveToPhotoGallery : true,
		success : function(event) {			
			getTextInImage(event.media.nativePath);
		},
		cancel : function() {
		},
		error : function(error) {
			var a = Titanium.UI.createAlertDialog({
				title : 'Video'
			});
			if (error.code == Titanium.Media.NO_VIDEO) {
				a.setMessage('Device does not have video recording capabilities');
			} else {
				a.setMessage('Unexpected error: ' + error.code);
			}
			a.show();
		},
		//overlay : overlay,
		showControls : false,
		mediaTypes : Titanium.Media.MEDIA_TYPE_PHOTO,
		autohide : false
	};
	cameraButton.addEventListener('click', function(e) {
		Titanium.Media.showCamera(cameraConfig);
	});

}
# flutter_video_info

This plugin uses MediaMetadataRetriever class of native android to get basic meta information 
of a video file.


<b>The following info can be extracted by this plugin:</b>

`title`
`path`
`author`
`mimetype`
`height`
`width`
`filesize`
`duration`
`orientation`
`date`
`framerate`
`location`



## Installation & Uses

Add `flutter_video_info` as a dependency in your pubspec.yaml file ([what?](https://flutter.io/using-packages/)).
```
dependencies:
    flutter_video_info: <current version>
```

Import FlutterVideoInfo in your dart file.
```dart
import 'package:flutter_video_info/flutter_video_info.dart';

final videoInfo = FlutterVideoInfo();

String videoFilePath = "your_video_file_path";
var info = await videoInfo.getVideoInfo(videoFilePath);

//String title = info.title;   to get title of video
//similarly path,author,mimetype,height,width,filesize,duration,orientation,date,framerate,location can be extracted.

```

## for iOS
If you select a video from your photo library, 
Add the following keys to your Info.plist file, located in /ios/Runner/Info.plist:

NSPhotoLibraryUsageDescription - describe why your app needs permission for the photo library. This is called Privacy - Photo Library Usage Description in the visual editor.
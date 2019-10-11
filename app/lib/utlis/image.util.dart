import 'dart:convert';
import 'dart:io';

import 'package:image/image.dart';

class ImageUtil {
  static Future<String> convertImages(String code, File file) async {
    Image image = decodeImage(file.readAsBytesSync());
    Image thumbnail = copyResize(image, height: 512);
    List<int> imageBytes = encodeJpg(thumbnail);
    String base64Image = base64Encode(imageBytes);
    return "data:image/jpg;base64," + base64Image;
  }
}

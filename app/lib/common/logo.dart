import 'package:flutter/material.dart';

class LogoImageWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    AssetImage logoAsset = AssetImage('images/logo.png');
    Image image = Image(image: logoAsset, width: 150.0, height: 150.0);
    return Container(
      child: image,
    );
  }
}

import 'dart:io';

import 'package:flutter/material.dart';

class ImageWidget extends StatelessWidget {
  final File file;

  ImageWidget({this.file});

  @override
  Widget build(BuildContext context) {
    if (this.file != null) {
      return Container(
        child: Image.file(file),
      );
    }
    return Container();
  }
}
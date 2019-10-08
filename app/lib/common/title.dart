import 'package:flutter/material.dart';

class TitleWidget extends StatelessWidget {
  final String text;

  TitleWidget({this.text});

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      style: TextStyle(
        fontSize: 25,
        fontWeight: FontWeight.bold,
      ),
    );
  }
}

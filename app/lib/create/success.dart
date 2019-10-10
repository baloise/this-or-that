import 'package:flutter/material.dart';
import 'package:qr_flutter/qr_flutter.dart';

class SuccessWidget extends StatelessWidget {
  final String title;
  final String code;

  SuccessWidget({this.title, this.code});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Created Survey"),
        actions: <Widget>[],
      ),
      body: SafeArea(
        child: Center(
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Column(
              children: <Widget>[
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 40, 0, 20),
                  child: Text(
                    title,
                    style: TextStyle(
                      fontSize: 25,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 40, 0, 20),
                  child: QrImage(
                    data: code,
                    version: QrVersions.auto,
                    size: 300.0,
                  ),
                ),
                Text(
                  code,
                  style: TextStyle(
                    fontSize: 45,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

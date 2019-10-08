import 'package:flutter/material.dart';

class OrDividerWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Flexible(
            child: Divider(
          color: Colors.grey[500],
        )),
        Padding(
          padding: const EdgeInsets.all(30.0),
          child: Text('OR'),
        ),
        Flexible(
            child: Divider(
          color: Colors.grey[500],
        )),
      ],
    );
  }
}
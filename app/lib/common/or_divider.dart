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
            child: Container(
              decoration: new BoxDecoration(
                color: Color.fromRGBO(54, 54, 54, 1.0),
                shape: BoxShape.circle,
              ),
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text("OR", style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
              ),
            )),
        Flexible(
            child: Divider(
          color: Colors.grey[500],
        )),
      ],
    );
  }
}

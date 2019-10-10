import 'package:flutter/material.dart';

import 'service/api-service.dart';
import 'service/dtos.dart';
import 'service/local-storage-service.dart';

class HistoryScreen extends StatefulWidget {
  HistoryScreen({Key key}) : super(key: key);

  @override
  HistoryScreenState createState() => HistoryScreenState();
}

class HistoryScreenState extends State<HistoryScreen> {

  HistoryScreenState({Key key}) : super();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.blueAccent[700],
          title: Text("History"),
        ),
        body: SafeArea(
          child: FutureBuilder<List<String>>(
          future: LocalStorageService.getParticipated(),
              builder: (context, snapshot) {
                  return Text(snapshot.data.length.toString());
              }
          )
        )
    );
  }
}

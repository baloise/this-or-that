import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import 'results.dart';
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

  static String buildSubtitle(DateTime dateTime) {
    var dateFormat = new DateFormat("dd.MM.yyyy");
     var date = dateFormat.format(dateTime);
     return "Date: " + date;
  }

  void openResults(String surveyId) {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => ResultScreen(surveyCode: surveyId)));
  }

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
                  if (snapshot.hasData) {
                    return ListView.separated(
                      separatorBuilder: (context, index) => Divider(
                        color: Colors.black,
                      ),
                      padding: EdgeInsets.all(8.0),
                      itemCount: snapshot.data.length,
                      itemBuilder: (context, index) {
                        return ListTile(
                          onTap: () {
                            openResults(ParticipatedSurvey.deserialize(
                                snapshot.data[index])
                                .surveyId);
                          },
                          leading: Text(ParticipatedSurvey.deserialize(
                              snapshot.data[index])
                              .surveyId, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                          title: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: <Widget>[
                              Text(
                                  ParticipatedSurvey.deserialize(
                                          snapshot.data[index])
                                      .perspective,
                                  style: TextStyle(fontSize: 20)),
                              Text(
                                  buildSubtitle(
                                      ParticipatedSurvey.deserialize(
                                              snapshot.data[index])
                                          .dateTime),
                                  style: TextStyle(fontSize: 20)),
                            ],
                          ),
                        );
                      },
                    );
                  }
                  if (snapshot.hasError) {
                    return ErrorWidget();
                  }
                  if (snapshot.connectionState == ConnectionState.done && snapshot.data == null) {
                    return NoHistoryYetWidget();
                  }

                  return Center(child: CircularProgressIndicator());
                })));
  }
}

class NoHistoryYetWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: <Widget>[
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: Text("No history is available yet. Please take part in a survey first.",
              style: TextStyle(fontSize: 20)),
        ),
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: MaterialButton(
            height: 60,
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text("Go back", style: TextStyle(fontSize: 20)),
            color: Colors.blueAccent[700],
            textColor: Colors.white,
            splashColor: Colors.white,
            minWidth: double.infinity,
          ),
        ),
      ],
    );
  }
}

class ErrorWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: <Widget>[
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: Text("The history could not be loaded.",
              style: TextStyle(fontSize: 20)),
        ),
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: MaterialButton(
            height: 60,
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text("Go back", style: TextStyle(fontSize: 20)),
            color: Colors.blueAccent[700],
            textColor: Colors.white,
            splashColor: Colors.white,
            minWidth: double.infinity,
          ),
        ),
      ],
    );
  }
}

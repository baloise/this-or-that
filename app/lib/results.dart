import 'package:flutter/material.dart';

import 'service/api-service.dart';
import 'service/dtos.dart';

class ResultScreen extends StatefulWidget {
  ResultScreen({Key key, @required this.surveyCode}) : super(key: key);

  final String surveyCode;

  @override
  ResultScreenState createState() => ResultScreenState(surveyCode: surveyCode);
}

class ResultScreenState extends State<ResultScreen> {
  final String surveyCode;

  Future<ScoreSummary> scoreSummaryFuture;

  ResultScreenState({Key key, @required this.surveyCode}) : super();

  String buildImageUrl(String imageId) {
    return ApiService.buildImageUrl(surveyCode, imageId);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.blueAccent[700],
          title: Text("Results (Code: " + this.surveyCode + ")"),
        ),
        body: SafeArea(
            child: FutureBuilder<ScoreSummary>(
                future: ApiService.fetchScoreData(this.surveyCode),
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.done &&
                      snapshot.hasData) {
                    return Padding(
                      padding: const EdgeInsets.all(20.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: <Widget>[
                          Text("Topic: " + snapshot.data.perspective,
                              style: TextStyle(
                                  fontSize: 20, fontWeight: FontWeight.bold)),
                          Text(
                              "Number of users: " +
                                  snapshot.data.numberOfUsers.toString(),
                              style: TextStyle(fontSize: 20)),
                          Text(
                              "Number of votes: " +
                                  snapshot.data.numberOfVotes.toString(),
                              style: TextStyle(fontSize: 20)),
                          Padding(
                            padding: const EdgeInsets.fromLTRB(0, 20, 0, 0),
                            child: Text("Results:",
                                style: TextStyle(
                                    fontSize: 20, fontWeight: FontWeight.bold)),
                          ),
                          Flexible(
                            child: ListView.builder(
                              itemCount: snapshot.data.scores.length,
                              itemBuilder: (context, index) {
                                return Card(
                                  child: Column(
                                    children: <Widget>[
                                      Image.network(buildImageUrl(
                                          snapshot.data.scores[index].imageId)),
                                      Text("Score: " +
                                          snapshot.data.scores[index].score
                                              .toString())
                                    ],
                                  ),
                                );
                              },
                            ),
                          )
                        ],
                      ),
                    );
                  } else if (snapshot.hasError) {
                    print(snapshot.error);
                    if (snapshot.error.toString().contains(NOT_CLOSED_ERR)) {
                      return NotClosedYetWidget();
                    }
                    return ErrorWidget();
                  }

                  return Center(child: CircularProgressIndicator());
                })));
  }
}

class NotClosedYetWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: <Widget>[
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: Text("The survey is not closed yet, so no results can be loaded at this time.",
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
          child: Text("The results could not be loaded.",
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

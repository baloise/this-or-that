import 'package:flutter/material.dart';
import 'package:this_or_that_app/service/api-service.dart';

import 'common/or_divider.dart';
import 'common/or_divider_vertical.dart';
import 'results.dart';
import 'service/dtos.dart';

const double OR_DIVIDER_PADDING = 30.0;

class VoteScreen extends StatefulWidget {
  VoteScreen({Key key, @required this.surveyCode}) : super(key: key);

  final String surveyCode;

  @override
  VoteScreenState createState() => VoteScreenState(surveyCode: surveyCode);
}

class VoteScreenState extends State<VoteScreen> {
  final String surveyCode;

  String firstElement;
  String secondElement;
  Future<DecisionSet> decisionSetFuture;

  VoteScreenState({Key key, @required this.surveyCode}) : super();

  @override
  void initState() {
    this.loadNewDecisionSet();
  }

  void loadNewDecisionSet() {
    setState(() {
      decisionSetFuture = ApiService.fetchNewDecisionSet(surveyCode);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Survey (Code: " + surveyCode + ")"),
          actions: <Widget>[
            // IconButton(
            //     icon: Icon(Icons.close),
            //     tooltip: 'Close survey',
            //     onPressed: null)
          ],
        ),
        body: SafeArea(
            child: FutureBuilder<DecisionSet>(
                future: decisionSetFuture,
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.done &&
                      snapshot.hasData) {
                    if (snapshot.data.surveyIsRunning) {
                      return new OrientationBuilder(
                          builder: (context, orientation) {
                        if (orientation == Orientation.portrait) {
                          return new Column(
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                VoteImageWidget(
                                  surveyId: surveyCode,
                                  imageId: snapshot.data.id1,
                                  winnerCallback: () {
                                    voteFor(new DecisionChoice(
                                        winner: snapshot.data.id1,
                                        loser: snapshot.data.id2));
                                  },
                                ),
                                Padding(
                                  padding:
                                      const EdgeInsets.all(OR_DIVIDER_PADDING),
                                  child: OrDividerWidget(),
                                ),
                                VoteImageWidget(
                                  surveyId: surveyCode,
                                  imageId: snapshot.data.id2,
                                  winnerCallback: () {
                                    voteFor(new DecisionChoice(
                                        winner: snapshot.data.id2,
                                        loser: snapshot.data.id1));
                                  },
                                ),
                              ]);
                        }
                        return new Row(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              VoteImageWidget(
                                surveyId: surveyCode,
                                imageId: snapshot.data.id1,
                                winnerCallback: () {
                                  voteFor(new DecisionChoice(
                                      winner: snapshot.data.id1,
                                      loser: snapshot.data.id2));
                                },
                              ),
                              Padding(
                                padding:
                                    const EdgeInsets.all(OR_DIVIDER_PADDING),
                                child: OrDividerVerticalWidget(),
                              ),
                              VoteImageWidget(
                                surveyId: surveyCode,
                                imageId: snapshot.data.id2,
                                winnerCallback: () {
                                  voteFor(new DecisionChoice(
                                      winner: snapshot.data.id2,
                                      loser: snapshot.data.id1));
                                },
                              ),
                            ]);
                      });
                    } else {
                      return FinishedWidget(
                        resultCallback: openResults,
                      );
                    }
                  } else if (snapshot.hasError) {
                    return ErrorWidget();
                  }

                  return Center(child: CircularProgressIndicator());
                })));
  }

  voteFor(DecisionChoice choice) {
    print("Choice: " + choice.winner);

    ApiService.postDecisionChoice(surveyCode, choice);
    loadNewDecisionSet();
  }

  openResults() {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => ResultScreen(surveyCode: surveyCode)));
  }
}

class FinishedWidget extends StatelessWidget {
  final VoidCallback resultCallback;

  FinishedWidget({this.resultCallback});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: <Widget>[
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: Text("The survey was closed.", style: TextStyle(fontSize: 20)),
        ),
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: MaterialButton(
            height: 60,
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text("Back to homepage", style: TextStyle(fontSize: 20)),
            color: Colors.blueAccent[700],
            textColor: Colors.white,
            splashColor: Colors.white,
            minWidth: double.infinity,
          ),
        ),
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: MaterialButton(
            height: 60,
            onPressed: resultCallback,
            child: Text("View results", style: TextStyle(fontSize: 20)),
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
          child: Text("The requested survey could not be found.",
              style: TextStyle(fontSize: 20)),
        ),
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: MaterialButton(
            height: 60,
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text("Back to homepage", style: TextStyle(fontSize: 20)),
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

class VoteImageWidget extends StatelessWidget {
  final String surveyId;
  final String imageId;
  final VoidCallback winnerCallback;

  VoteImageWidget({this.surveyId, this.imageId, this.winnerCallback});

  String buildImageUrl() {
    return ApiService.buildImageUrl(surveyId, imageId);
  }

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: InkWell(
          onTap: winnerCallback,
          child: Image.network(
            buildImageUrl(),
            fit: BoxFit.fitWidth,
            loadingBuilder: (BuildContext context, Widget child,
                ImageChunkEvent loadingProgress) {
              if (loadingProgress == null) return child;
              return Center(
                child: CircularProgressIndicator(
                  value: loadingProgress.expectedTotalBytes != null
                      ? loadingProgress.cumulativeBytesLoaded /
                          loadingProgress.expectedTotalBytes
                      : null,
                ),
              );
            },
          ),
        ),
      ),
    );
  }
}

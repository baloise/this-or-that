class DecisionSet {

  String id1;
  String id2;
  bool surveyIsRunning;
  String perspective;

  DecisionSet({this.id1, this.id2, this.surveyIsRunning, this.perspective});

  factory DecisionSet.fromJson(Map<String, dynamic> json) {
    return DecisionSet(
      id1: json['id1'],
      id2: json['id2'],
      surveyIsRunning: json['surveyIsRunning'],
      perspective: json['perspective'],
    );
  }

}

class DecisionChoice {

  String winner;
  String loser;

  DecisionChoice({this.winner, this.loser});

  Map toMap() {
    var map = new Map<String, dynamic>();
    map["winner"] = winner;
    map["loser"] = loser;

    return map;
  }

}
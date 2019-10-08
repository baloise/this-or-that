import 'dart:convert';

import 'package:http/http.dart' as http;

import 'dtos.dart';

const API_BASE_URL = "http://api.holzenkamp.me/spring-test";

class ApiService {

  /*
  static DecisionSet fetchNewDecisionSet(String surveyId) {
    String url = API_BASE_URL + "/" + surveyId + "/vote";
    final response = http.get(url).then((response) {
      print("=>" + response.body);
      if (response.statusCode == 200) {
        return DecisionSet.fromJson(json.decode(response.body));
      } else {
        throw Exception("Error"); // FIXME
      }
    });
  }
  */

  static Future<DecisionSet> fetchNewDecisionSet(String surveyId) async {
    String url = API_BASE_URL + "/" + surveyId + "/vote";
    final response = await http.get(url);

    print("=>" + response.body);

    if (response.statusCode == 200) {
      return DecisionSet.fromJson(json.decode(response.body));
    } else {
      throw Exception("Error"); // FIXME
    }
  }

  static Future<void> postDecisionChoice(String surveyId, DecisionChoice choice) async {
    String url = API_BASE_URL + "/" + surveyId + "/vote";
    String body = json.encode(choice.toMap());
    print("POST-BODY: "+ body);

    return http.post(url, body: body).then((http.Response response) {
      if (response.statusCode != 200) {
        throw new Exception("Error"); // FIXME
      }
    });
  }

  static String buildImageUrl(String surveyId, String imageId) {
    return API_BASE_URL + "/" + surveyId + "/image/" + imageId;
  }

}

import 'package:shared_preferences/shared_preferences.dart';

const STORAGE_KEY = "thisOrThatHistory";

class LocalStorageService {

  static void saveParticipated(String surveyId) {
    SharedPreferences.getInstance().then((instance) {
      List<String> store;

      try {
         store = instance.getStringList(STORAGE_KEY);
      } catch (Exception) {
        store = [];
      }

      if (store == null) {
        store = [surveyId];
      } else {
        store.add(surveyId);
      }

      instance.setStringList(STORAGE_KEY, store);
      print("wrote " + store.toString() + " to store");
    });
  }

  static Future<List<String>> getParticipated() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getStringList(STORAGE_KEY);
  }

}
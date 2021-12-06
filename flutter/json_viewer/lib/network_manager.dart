import 'package:http/http.dart' as http;

class NetworkManager {
  Future<String?> loadStringData(String url) async {
    try {
      final response = await http.get(Uri.parse(url));
      if (response.statusCode == 200) {
        return response.body.isEmpty ? null : response.body;
      } else {
        return null;
      }
    } catch (e) {
      return null;
    }
  }
}

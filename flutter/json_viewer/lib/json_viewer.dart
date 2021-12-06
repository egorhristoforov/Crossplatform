import 'dart:convert';

import 'package:fluttertoast/fluttertoast.dart';
import 'package:flutter/material.dart';
import './network_manager.dart';
import './share_manager.dart';

class JsonViewerPage extends StatefulWidget {
  const JsonViewerPage({Key? key}) : super(key: key);

  final String title = "JSON Viewer";

  @override
  State<JsonViewerPage> createState() => _JsonViewerPageState();
}

class _JsonViewerPageState extends State<JsonViewerPage> {
  late TextEditingController _urlInputController;
  late TextEditingController _jsonInputController;

  bool _jsonEditorVisible = false;

  final NetworkManager _networkManager = NetworkManager();
  final ShareManager _shareManager = ShareManager();

  @override
  void initState() {
    _urlInputController = TextEditingController();
    _jsonInputController = TextEditingController();

    super.initState();
  }

  @override
  void dispose() {
    _urlInputController.dispose();
    _jsonInputController.dispose();

    super.dispose();
  }

  void _setJsonEditorVisible(bool visible) {
    setState(() {
      _jsonEditorVisible = visible;
    });
  }

  Future<void> _didTapLoadButton() async {
    _setJsonEditorVisible(false);
    _loadJSON();
  }

  void _loadJSON() async {
    String url = _urlInputController.text;
    String? data = await _networkManager.loadStringData(url);

    if (data == null) {
      _showToast("Loading error", Colors.red);
      return;
    }

    if (!_isValidJSON(data)) {
      _showToast("Invalid JSON", Colors.red);
      return;
    }

    _jsonInputController.text = data;
    _setJsonEditorVisible(true);
  }

  void _didTapSaveButton() {
    String text = _jsonInputController.text;

    if (!_isValidJSON(text)) {
      _showToast("Invalid JSON", Colors.red);
      return;
    }

    _shareManager.share(text);
  }

  bool _isValidJSON(String text) {
    try {
      json.decode(text) as Map<String, dynamic>;
      return true;
    } on FormatException {
      return false;
    }
  }

  void _showToast(String text, Color color) {
    Fluttertoast.showToast(
        msg: text,
        toastLength: Toast.LENGTH_LONG,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 3,
        backgroundColor: color,
        textColor: Colors.white,
        fontSize: 16.0);
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
      child: Scaffold(
        appBar: AppBar(
          title: Text(widget.title),
        ),
        body: Container(
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                TextField(
                  controller: _urlInputController,
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: "Enter URL",
                  ),
                  autocorrect: false,
                ),
                ElevatedButton(
                    onPressed: _didTapLoadButton,
                    child: const Text("Load JSON")),
                if (_jsonEditorVisible)
                  Column(
                    children: [
                      TextField(
                        controller: _jsonInputController,
                        keyboardType: TextInputType.multiline,
                        minLines: 5,
                        maxLines: null,
                        decoration:
                            const InputDecoration(border: OutlineInputBorder()),
                        autocorrect: false,
                      ),
                      ElevatedButton(
                          onPressed: _didTapSaveButton,
                          child: const Text("Save JSON")),
                    ],
                  )
              ],
            ),
          ),
          constraints: const BoxConstraints.expand(),
          padding: const EdgeInsets.all(16),
        ),
      ),
    );
  }
}

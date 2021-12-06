const axios = require('axios').default;
const fileSaver = require('file-saver');

let loadButton = document.getElementById("loadButton");
let urlField = document.getElementById("urlField");

let jsonTextArea = document.getElementById("jsonTextarea");
let saveButton = document.getElementById("saveButton");

const setJSONEditorHidden = (hidden) => {
    jsonTextArea.style.visibility = hidden ? 'hidden' : 'visible';
    saveButton.style.visibility = hidden ? 'hidden' : 'visible';
};

const handleJSONLoaded = (data) => {
    setJSONEditorHidden(false);

    var pretty = JSON.stringify(data, undefined, 4);
    jsonTextArea.value = pretty;
};

const isJSONValid = (json) => {
    if (typeof json == 'string') {
        try {
            JSON.parse(json);
        } catch (e) {
            return false;
        }
    } else if (typeof json != 'object' || json == null) {
        return false;
    }

    return true;
}

const saveTextToFile = (text) => {
    var blob = new Blob([text]);
    fileSaver.saveAs(blob);
}

loadButton.addEventListener('click', () => {
    setJSONEditorHidden(true);
    let url = urlField.value;
    
    axios.get(url, {responseType: 'text'})
    .then(function (response) {
        let json = response.data;

        if (isJSONValid(json)) {
            handleJSONLoaded(json);
        } else {
            alert("Not JSON loaded!");
        }
      })
    .catch(function (error) {
        alert(error + "123");
    })
});

saveButton.addEventListener('click', () => {
    if (!isJSONValid(jsonTextArea.value)) {
        alert("Invalid JSON!");
        return;
    }

    saveTextToFile(jsonTextArea.value);
});

setJSONEditorHidden(true);
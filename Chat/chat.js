var id = function() {
    var date = Date.now();
    var random = Math.random() * Math.random();

    return Math.floor(date * random).toString();
};
var newMessage = function(text, user) {
    return {
        messageText: text,
        user: user,
        id: id(),
        check: true
    };
};

var messageList = [];
var username = '';

function setMarker(labelEl) {
    if (labelEl.classList.contains('Check'))
        labelEl.classList.remove('Check');
    else labelEl.classList.add('Check');
}
function run() {
    if(JSON.parse(localStorage.getItem("Chat username"))) {
        username = JSON.parse(localStorage.getItem("Chat username"));
        document.getElementById('user').value = username;
        document.getElementsByClassName('check')[0].style.visibility = 'visible';
    }
    var dialogContainer = document.getElementsByClassName('dialog')[0];
    dialogContainer.addEventListener('click', delegateEvent);
    dialogContainer.addEventListener('change', delegateEvent);
    var allMessages = restore();
    createAllMessages(allMessages);
}
function delegateEvent(evtObj) {
    if (evtObj.type == 'click' && evtObj.target.className == 'sendButton') {
        if(!document.getElementById('user').value) {
            alert("Please, Input Username :)");
            return;
        }
        document.getElementsByClassName('check')[0].style.visibility = 'visible';
        onSendMessageButtonClick(evtObj);
    }
    if (evtObj.type == 'click' && evtObj.target.className == 'userNameButton' ) {
        onUsernameButtonClick(evtObj);
    }
    if (evtObj.type === 'change' && evtObj.target.nodeName == 'INPUT') {
        var labelEl = evtObj.target.parentElement;
        setMarker(labelEl);
    }
    if (evtObj.type == 'click' && evtObj.target.nodeName == 'INPUT' && evtObj.target.id == 'user') {
        onUsernameChangeClick(evtObj);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('delete')) {

        deleteMsg(evtObj);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('edit')) {
        editMsg(evtObj);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('messages')) {
        selectMsg(evtObj);
    }
}

function createMessage(message) {
    var divMessage = document.createElement('div');
    divMessage.id = message.id;
    var textMessage = document.createElement('p');
    divMessage.classList.add('messageStyle');
    textMessage.setAttribute('type', 'value');
    var checkbox = document.createElement('input');
    checkbox.setAttribute('type', 'checkbox');
    divMessage.appendChild(textMessage);
    divMessage.appendChild(checkbox);
    divMessage.appendChild(document.createTextNode(message.user + ' :   ' + message.messageText));

    return divMessage;
}

function createAllMessages(allMessages) {
    for (var i = 0; i < allMessages.length; i++) {
        messageList.push(allMessages[i]);
        addMessage(allMessages[i]);
    }
    if (allMessages.length > 1)
        username = allMessages[allMessages.length - 1].name;
}
function deleteMsg() {
    var messages = document.getElementsByClassName('Check');
    if (messages.length != 0) {
        for (var i = messages.length - 1; i >= 0; i--) {
            var id = messages[i].id;
            for (var j = 0; j < messageList.length; j++) {
                if (id == messageList[j].id) {
                    messageList[j].message = 'deleted';
                }
            }
            var message = messages[i].innerHTML;
            var pos = message.lastIndexOf(':') + 4;
            var oldMessage = message.substring(pos);
            message = message.replace(oldMessage, 'deleted');
            messages[i].innerHTML = message;
            setMarker(messages[i]);
        }
    }
    store(messageList);
}
function editMsg() {
    var messages = document.getElementsByClassName('Check');
    if (messages.length > 1) {
        alert("You chose too much messages! Please choose one");
        return;
    }
    var messageIn =  messages[0].innerHTML;
    var pos = messageIn.lastIndexOf(':') + 4;
    var messageOut = messageIn.substring(pos);
    var messageNew = prompt("Edit message:", messageOut);
    var id = messages[0].id;
    for (var i = 0; i < messageList.length; i++) {
        if (id == messageList[i].id) {
            messageList[i].message = messageNew;
        }
    }
    messageIn = messageIn.replace(messageOut, messageNew);
    messages[0].innerHTML = messageIn;
    setMarker(messages[0]);
    store(messageList);
}
function addMessage(mesObj) {
    if (!mesObj.messageText) {
        return;
    }
    var this_message = createMessage(mesObj);
    var messages = document.getElementsByClassName('messages')[0];
    messages.appendChild(this_message);
}
function onUsernameChangeClick() {
    var check = document.getElementsByClassName('check')[0];
    check.style.visibility = 'hidden';
}
function onUsernameButtonClick() {
    if (!document.getElementById('user').value) {
        return;
    }
    var check = document.getElementsByClassName('check')[0];
    check.style.visibility = 'visible';
    username = document.getElementById('user').value;
    localStorage.setItem("Chat username", JSON.stringify(username));
}
function onSendMessageButtonClick() {
    var message = document.getElementById('windowMessage');
    var newMess = newMessage(message.value, username);
    messageList.push(newMess);
    addMessage(newMess);
    message.value = '';
    store(messageList);
}
function store(listToSave) {

    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }
    localStorage.setItem("Nice Chat", JSON.stringify(listToSave));
}
function restore() {
    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }

    var item = localStorage.getItem("Nice Chat");

    return item && JSON.parse(item);
}
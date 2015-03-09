function run(){
    var user_name;

    var appContainer = document.getElementsByClassName('dialog')[0];
    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('change', delegateEvent);
}

function delegateEvent(evtObj) {
    if(evtObj.type === 'click' && evtObj.target.classList.contains('sendButton')){
        onSendButtonClick(evtObj);
    }
    if(evtObj.type === 'change' && evtObj.target.nodeName == 'INPUT'){
        var labelEl = evtObj.target.parentElement;
        setMarkerDelete(labelEl);
    }
    if(evtObj.type === 'click' && evtObj.target.classList.contains('deleteButton')){
        deleteMess(evtObj);
    }
}

function onSendButtonClick(){
    var message = document.getElementById('windowMessage');

    addMessage(message.value);
    message.value = '';
}

function addMessage(value) {
    if(!value){
        return;
    }

    var item = createItem(value);
    var items = document.getElementsByClassName('messages')[0];

    items.appendChild(item);
}

function createItem(text){
    var divItem = document.createElement('div');
    var textM = document.createElement('p');
    var checkbox = document.createElement('input');

    var date = new Date();
    var dateString = date.toLocaleTimeString();

    divItem.classList.add('yourMesStyle');
    textM.setAttribute('type', 'text');
    checkbox.setAttribute('type', 'checkbox');


    divItem.appendChild(textM);
    divItem.appendChild(checkbox);
    divItem.appendChild(document.createTextNode(dateString + " : " + user_name + ' :   ' + text ));

    return divItem;
}

function Input(){
    user_name = "";
    user_name = prompt("Please, Input Your Name","");
    user_name = user_name.toLowerCase();
    alert ("Nice to meet you, " + user_name);
}

function Change(){
    user_name = prompt("Please, Change Your Name","");
    user_name = user_name.toLowerCase();
    alert ("Nice to meet you, " + user_name);
}


function setMarkerDelete(labelEl) {
    if(labelEl.classList.contains('forDelete')) {
        labelEl.classList.remove('forDelete');
    }
    else {
        labelEl.classList.add('forDelete');
    }
}


function deleteMess(){
    var messages = document.getElementsByClassName('forDelete');
    if(messages.length != 0)
        for (var i = messages.length - 1;  i >= 0; i--)
        {
            messages[i].remove();
        }
}

jsHover = function() {
    var hEls = document.getElementById("nav").getElementsByTagName("LI");
    for (var i=0, len=hEls.length; i<len; i++) {
        hEls[i].onmouseover=function() { this.className+=" jshover"; }
        hEls[i].onmouseout=function() { this.className=this.className.replace(" jshover", ""); }
    }
}
if (window.attachEvent && navigator.userAgent.indexOf("Opera")==-1) window.attachEvent("onload", jsHover);
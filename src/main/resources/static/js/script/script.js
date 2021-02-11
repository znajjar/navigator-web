var stompClient = null;
var gameId = null;
var userName = null;

function connect() {
    userName = $('#usernameField').val();
    console.log(userName)
    if (userName.length === 0) {
        return;
    }
    var socket = new SockJS('/request');
    stompClient = Stomp.over(socket);
    stompClient.connect({username: userName}, function () {
        stompClient.subscribe('/user/queue/event/player', handlePlayerEvent);
        stompClient.subscribe('/user/queue/event/game/list', handleListEvent);
        stompClient.subscribe('/user/queue/event/game/joined', handleJoinEvent);
        // stompClient.subscribe('/user/queue/event/game/update', handleListEvent)
        stompClient.subscribe('/topic/games/update', handleListEvent);
        hideAllScreens();
        showJoinGame();
        requestGamesList();
    }, function (err) {
        console.log(err);
    });
}

function hideAllScreens() {
    $("#loginFormScreen").hide();
    $("#joinGameScreen").hide();
    $("#waitingScreen").hide();
    $("#startGameScreen").hide();
    $("#navigatingScreen").hide();
    $("#gameEndedScreen").hide();
}


function showJoinGame() {
    $('#joinGameScreen').show()
}

function requestGamesList() {
    stompClient.send('/app/request/game/list')
}

function newGame() {
    stompClient.send('/app/request/game/create')
}

function startGame() {
    stompClient.send('/app/request/game/start', {}, gameId);
}

function joinGame(gameId) {
    stompClient.send('/app/request/game/join', {}, gameId);
}

function handleJoinEvent(event) {
    hideAllScreens();
    const joinEvent = JSON.parse(event.body)
    gameId = joinEvent.gameId
    stompClient.unsubscribe('/topic/games/update')
    if (joinEvent.host) {
        $('#startGameScreen').show();
    } else {
        $('#waitingScreen').show();
    }
}

function handleListEvent(event) {
    const gamesList = JSON.parse(event.body);
    // document.getElementById('gameList')
    $('#gameList').empty()
    gamesList.forEach(addGameNode)
}

function addGameNode(gameId) {
    const gameNode = createGameNode(gameId);
    document.getElementById('gameList').appendChild(gameNode);
}

function createGameNode(gameId) {
    const node = document.createElement('li');
    const text = document.createTextNode(gameId);
    const button = document.createElement('button');
    // button.setAttribute('onclick', 'joinGame(' + gameId + ')');
    button.onclick = function () {
        joinGame(gameId)
    };
    button.textContent = 'join';
    node.appendChild(text)
    node.appendChild(button)
    return node;
}

function handlePlayerEvent(event) {
    const playerEvent = JSON.parse(event.body);
    console.log(playerEvent)
    if (playerEvent.type === 'event') {
        switch (playerEvent.eventType) {
            case 'gameStarted':
                hideAllScreens();
                $('#navigatingScreen').show();
                break;
            case 'gameWon':
                hideAllScreens();
                $("gameEndedText").text("you won the game");
                $("#gameEndedScreen").show();
                break;
            case 'gameLost':
                hideAllScreens();
                $("gameEndedText").text("you lost the game");
                $("#gameEndedScreen").show();
                break;
        }
    }
}

function lookCommand() {
    sendCommand('look');
}

function checkCommand() {
    sendCommand('check');
}

function turnRightCommand() {
    sendCommand('turnRight');
}

function turnLeftCommand() {
    sendCommand('turnLeft');
}

function moveForwardCommand() {
    sendCommand('moveForward');
}

function moveBackwardCommand() {
    sendCommand('moveBackward');
}

function switchLightsCommand() {
    sendCommand('switchLights');
}

function useFlashlightCommand() {
    sendCommand('useFlashlight');
}

function useKeyCommand() {
    const keyName = $('#keyNameField').val();
    sendCommandWithArgs('useKey', [keyName])
}

function sendCommand(command) {
    sendCommandWithArgs(command, [])
}

function sendCommandWithArgs(command, args) {
    stompClient.send('/app/request/command', {}, JSON.stringify({name: command, args: []}));
}

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
    sendCommandWithArgs('useKey', [keyName + ' key']);
}

function tradeCommand() {
    sendCommand('trade');
}

function buyItemCommand() {
    const buyItemField = $("#buyItemField")
    const itemName = buyItemField.val();
    sendCommandWithArgs('buy', [itemName]);
    buyItemField.empty();
}

function sellItemCommand() {
    const itemName = $("#buyItemField").val();
    sendCommandWithArgs('sell', [itemName]);
}

function exitTradeCommand() {
    sendCommand('exitTrade');
}

function sendCommand(command) {
    sendCommandWithArgs(command, [])
}

function sendCommandWithArgs(command, args) {
    const request = {name: command, args: args};
    console.log(request);
    stompClient.send('/app/request/command', {}, JSON.stringify(request));
}
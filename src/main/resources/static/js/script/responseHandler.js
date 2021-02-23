const handlers = {};
handlers.look = handleLookResponse;
handlers.check = handleCheckResponse;
handlers.move = handleMoveResponse;
handlers.turn = handleTurnResponse;
handlers.flashlight = handleFlashlightResponse;
handlers.switchLights = handleSwitchLightsResponse;
handlers.trade = handleTradeResponse;
handlers.sell = handleSellResponse;
handlers.buy = handleBuyResponse;
handlers.exitTrade = handleExitTradeResponse;
handlers.status = handleStatusResponse;

var outputScreen = $("#outputScreen");

function hideGameScreens() {
    $("#navigatingScreen").hide();
    $("#tradingScreen").hide();
}

function handlePlayerResponse(playerResponse) {
    outputScreen.empty();
    if (!playerResponse.success) {
        outputScreen.text(playerResponse.cause);
        return;
    }
    const responseType = playerResponse.requestType;
    const handler = handlers[responseType];
    handler(playerResponse)
}

function handleLookResponse(playerResponse) {
    outputScreen.text(playerResponse.itemType)
}

function handleCheckResponse(playerResponse) {
    const checkItem = playerResponse.checkedItem;
    if (checkItem.isChecked) {
        outputScreen.text("items acquired: \n" + checkItem.acquiredItems);
    } else {
        outputScreen.text("item requires the " + checkItem.requiredKey + " key");
    }
}

function handleTurnResponse(playerResponse) {
    outputScreen.text('facing: ' + playerResponse.facingDirection);
}

function handleMoveResponse(playerResponse) {
    let nextRoom = playerResponse.nextRoom;
    let text = 'moved to room: ' + nextRoom.roomId + '. ';
    if (nextRoom.lightSwitch) {
        text += 'room has a light switch. ';
        if (nextRoom.lit) {
            text += 'room is lit';
        } else {
            text += 'room is not lit';
        }
    }
    outputScreen.text(text);
}

function handleSwitchLightsResponse(playerResponse) {
    let text = 'switched the lights. ';
    if (playerResponse.isLit) {
        text += 'room is now lit';
    } else {
        text += 'room is now not lit';
    }
    outputScreen.text(text);
}

function handleFlashlightResponse(playerResponse) {
    let flashlight = playerResponse.flashlight;
    let text = 'switched the flashlight. flashlight is now ';
    if (flashlight.light) {
        text += 'lit';
    } else {
        text += 'not lit';
    }
    outputScreen.text(text);
}

function handleTradeResponse(playerResponse) {
    hideGameScreens();
    $("#tradingScreen").show();
    outputScreen.text("trading");
}

function handleBuyResponse(playerResponse) {

}

function handleSellResponse(playerResponse) {

}

function handleExitTradeResponse(playerResponse) {
    hideGameScreens();
    $("#navigatingScreen").show();
    outputScreen.text("exited trading");
}

function handleStatusResponse(playerResponse) {
    outputScreen.text(playerResponse)
}
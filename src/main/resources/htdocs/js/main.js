/*
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
var wellcomeMessages = {};
var noteTimeout;
var gameOver = false;
var gameMode = 0;
var computerGameId = {};
var maxScore = {};
var modal = {};
var bundle = {};

var stringFormat = (string, args) => {
    var s = string;
    var i = !!args ? args.length : 0;
    while (i--) {
        s = s.replace(new RegExp('\\{' + i + '\\}', 'gm'), args[i]);
    }
    return s;
};

var setLang = (lang) => {
    jQuery.i18n.properties({
        name: 'messages', 
        path: 'bundle/', 
        mode: 'both',
        language: lang, 
        callback: () => {
            bundle = $.i18n.prop;
        }
    });
    var elements = $('[bundle]');
    var i = elements.length;
    while (i--) {
		var element = elements[i];
		if (!!element) {
			element.innerHTML = resourceBundle(element.getAttribute("bundle"));
		}
	}
}

var resourceBundle = (property, msgArgs) => {
	if (!!property) {
		var message = bundle(property);
		if (!!message) {
			return !!message.match(/{[0-9]+}/g) && !!msgArgs ? stringFormat(message, msgArgs) : message;
		}
		return property;
	}
	return "null";
}

var randomWellcomeMessage = (n) => {
	return resourceBundle("wellcome_message_"+ Math.floor((Math.random() * n)));
}

var gameCallback = (json) => {
	var weapon =  resourceBundle("weapon_" + json.weapon)
	var message = resourceBundle("resultCode_" + json.resultCode, [weapon]);
	showNote(message, json.result, noteTimeout);
	try {
		updateScores(json);
	} catch (e) {
		gameOver = true;
		if ("1" === json.mode) {
			stopComputerGame();
		}
		var winner = selectWinner(JSON.parse(e))
		var resultMessage = selectResultMessage(winner, json.mode);
		showModal(true, resultMessage, winner);
	}
};

var updateScores = (json) => {
	switch (parseInt(json.result)) {
	case 1:
		incrementScore(["score1"])
		break;
	case -1:
		incrementScore(["score2"])
		break;
	case 0:
		incrementScore(["score1", "score2"]);
		break;
	}
}

var selectResultMessage = (result, mode) => {
	if (result === 0) {
		return resourceBundle("game.tied", [resourceBundle("game.over")]);
	} else {
		if ("0" === mode) {
			return resourceBundle(result === 1 ? "you.win" : "you.lose", [resourceBundle("game.over")]);
		} else {
			var player =  result === 1 ? 1 : 2;
			return resourceBundle("player.win", [player, resourceBundle("game.over")]);
		}
	}
}

var selectWinner = (scores) => {
	return scores.length == 2 ? 0 : scores[0] === "score1" ? 1 : -1;
}

var configurationCallback = (json) => {
	if (!!json) {
		maxScore = json.maxScore;
		noteTimeout = json.noteTimeout;
		wellcomeMessages = parseInt(json.wellcomeMessages);
		$("#maxScoreValue").html(maxScore);
	}
}

var errorCallback = (xhr) => {	
	if (xhr.readyState == 4) {
		if (xhr.status >= 500) {
			showNote(resourceBundle("error.server"), 500, noteTimeout);
		} else {
			if (xhr.status >= 400 && xhr.status < 500) {
				showNote(resourceBundle("error.resources"), 0);
			} else {
				showNote(resourceBundle("error.connection"), 500, noteTimeout);
			}
		}	
	}
};

var showModal = (show, message, statusCode) => {
	modal.style.display = true == show ? "block" : "none";
	if (!!message) {
		setTimeout(() => {
			showNote(message, statusCode, 1e5);
		}, noteTimeout);
	}
}

var closeNote = () => {
	var note = document.getElementById("note");
	if (!!note) {
		note.style.visibility = "hidden";
	}
}

var showNote = (message, statusCode, timeout) => {
	var note = document.getElementById("note");
	if (!!note) {
		note.className = selectClass(statusCode);
		note.innerHTML = message;
		note.style.display = 'block';
		setTimeout(() => {
			note.style.display = 'none';
		}, timeout); // Hide after the given timeout seconds
	}
}

var selectClass = (statusCode) => {
	var cssClass = null;
	switch (parseInt(statusCode)) {
	case 1:
		cssClass = "note-success";
		break;
	case 0:
		cssClass = "note-info";
		break;
	case -1:
		cssClass = "note-warning";
		break;
	case 500:
		cssClass = "note-danger";
		break;
	}
	return cssClass;
}

var startGame = () => {
	// show wellcome message
	showNote(randomWellcomeMessage(1), 1, 2e3);

	// get/set configuration
	getJson("/rest/configuration", configurationCallback, errorCallback);
	
	// adding weapons event listeners
	var wps = document.getElementById("weapons");
	if (!!wps) {
		wps.addEventListener("click", (e) => {
			if (e.target.tagName === "IMG") {
				if (true === gameOver || computerGameId > 0) {
					e.preventDefault();
					return; // workaround: event propagation isn't stopping
				}
				getJson("/rest/" + e.target.id, gameCallback, errorCallback);
			}
		});
	}
	
	// adding buttons event listeners
	var btn = document.getElementById("buttons");
	if (!!btn) {
		btn.addEventListener("click", (e) => {
			if (e.target.tagName === "BUTTON") {
				var id = e.target.id === "0" ? "1" : "0";
				swapClass(id, e.target.id, "disabled", "active");
				newGame(e.target.id);
			}
		});
	}
	
	// adding languages event listeners
	var lang = document.getElementById("languages");
	if (!!lang) {
		lang.addEventListener("click", (e) => {
			if (e.target.tagName === "IMG") {
				setLang(e.target.id);
			}
		});
	}
	
	// get/set modal
	modal = document.getElementById('rps-modal')
}

var startComputerGame = () => {
	computerGameId = window.setInterval(() => {
		getJson("/rest/computer", gameCallback, errorCallback);
	}, eval(noteTimeout*1.5));
}

var stopComputerGame = () => {
	if (!!computerGameId) {
		window.clearInterval(computerGameId);
		computerGameId = 0;
	}
}

var newGame = (mode) => {
	gameMode = mode;
	stopComputerGame();
	update("name1", resourceBundle("page.player1_" + mode));
	update("name2", resourceBundle("page.player2_" + mode));
	if (mode === "1") {
		replaceClass(["rock","paper","scissors"], "fade", "disabled");
	} else {
		replaceClass(["rock","paper","scissors"], "disabled", "fade");
	}
	// reset the scores
	resetScores(["score1", "score2"]);
	// close modal
	showModal(false, null);
	// show wellcome message
	showNote(randomWellcomeMessage(wellcomeMessages), 1, noteTimeout);
	// starting new game, then gameOver is false
	gameOver = false;
	if ("1" === mode) {
		startComputerGame();
	}
}

var resetScores = (ids) => {
	if (!!ids) {
		ids.forEach((id) => {
			update(id, 0);
		});
	}
}

var incrementScore = (ids) => {
	if (!!ids) {
		var winners = [];
		ids.forEach((id) => {
			var score = document.getElementById(id);
			if (!!score) {
				var newScore = parseInt(score.innerHTML) + 1;
				score.innerHTML = newScore;
				if (newScore >= maxScore) {
					winners.push(id);
				}
			}
		});
		if (winners.length > 0) {
			throw JSON.stringify(winners);
		}
	}
}

var update = (id, value) => {
	if (!!id) {
		var element = document.getElementById(id);
		if (!!element) {
			element.innerHTML = value;
		}
	}
}

var swapClass = (id1, id2, cssClass1, cssClass2) => {
	removeClass([id1], cssClass2);
	removeClass([id2], cssClass1);
	addClass([id1], cssClass1);
	addClass([id2], cssClass2);
}

var addClass = (ids, newClass) => {
	replaceClass(ids, null, newClass);
}

var removeClass = (ids, oldClass) => {
	replaceClass(ids, oldClass, null);
}

var replaceClass = (ids, oldClass, newClass) => {
	if (!!ids) {
		ids.forEach((id) => {
			if (!!oldClass) {
				$("#" + id).removeClass(oldClass);
			}
			if (!!newClass) {
				$("#" + id).addClass(newClass);
			}
		});
	}
}

var get = (url, contentType, successCallback, errorCallback) => {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = () => {
		if (xhr.readyState == 4  && xhr.status == 200) {
			successCallback(JSON.parse(xhr.responseText));
		} else {
			errorCallback(xhr);
		}
	}
	xhr.open("GET", url, true);
	xhr.setRequestHeader("Accept", contentType);
	xhr.send(null);
}

var getJson = (url, successCallback, errorCallback) => {
	get(url, "application/json", successCallback, errorCallback);
}

var disableRefresh = (e) => {
	return ((e.which || e.keyCode) != 116) && (e.keyCode == 65 && e.ctrlKey);
}

window.onbeforeunload = () => {return resourceBundle("game.data.lost");};

(($) => {
	document.onkeydown = (e) => {return disableRefresh(e);}
	$(() => {
		setLang(document.documentElement.lang);
		startGame();
	});
})(jQuery);
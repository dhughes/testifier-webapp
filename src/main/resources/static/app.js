var stompClient = null;
var displayedAttempt = null;

SyntaxHighlighter.defaults['toolbar'] = false;

function template(strings, ...keys) {
    return (function(...values) {
        var dict = values[values.length - 1] || {};
        var result = [strings[0]];
        keys.forEach(function(key, i) {
            var value = Number.isInteger(key) ? values[key] : dict[key];
            result.push(value, strings[i + 1]);
        });
        return result.join('');
    });
}

var recordClosure = template`
    <span class="${'recordType'} ${'escapedRecordName'}">
        <span>${'displayName'}</span>
    </span>
`;

var resultClosure = template`
    <a id="${'id'}" class="result ${'result'}" href="#attempt">${'result'}</a>
`;

var attemptClosure = template`
    <div id="attempt">
        <img class="close" src="images/x.png" />
        <pre class="brush: java">${'code'}</pre>
        <pre class="exceptionMessage ${'result'}">${'exceptionMessage'}</pre>
        <pre class="stackTrace ${'result'}">${'stackTrace'}</pre>
    </div>
`;

var exceptionClosure = template`
    <div id="#exception">Exception goes here...</div>
`;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/notifications', function (message) {
            updateStudentRecord(JSON.parse(message.body));
        });
    });
}

function toKey(value){
    return value.replace(/([^a-zA-Z0-9_-]).*?/g, "_").replace(".", "dot");
}

function getOrCreateRecord(recordType, parentRecord, recordName, displayName) {
    // get a unique project name
    var escapedRecordName = toKey(recordName);

    // if we didn't get a special display name then use the record name
    if(displayName == undefined) displayName = recordName;

    var record = parentRecord.find("." + recordType + "." + escapedRecordName);

    if(!record.length){
        record = $(recordClosure({
            recordType: recordType,
            escapedRecordName: escapedRecordName,
            displayName: displayName
        }));

        parentRecord.append(record);
    }

    return record;

}

function getSignature(notification) {
    var name = "";

    if(notification.methodName.length > 0) {
        name = notification.methodName;
    } else {
        name = notification.className;
    }

    name += "(";

    name += notification.arguments.join(", ");

    name += ")";

    return name;
}

function getStackTrace(notification){
    var stackTrace = notification.exception.stackTrace;

    if(notification.exception.causedBy != null){
        stackTrace += "\r" + getStackTrace(notification.exception.causedBy);
    }

    return stackTrace;
}

function updateStudentRecord(notification){
    console.log(notification);

    // get or create the student record
    var studentRecord = getOrCreateRecord("student", $("#studentRecords"), notification.studentEmail, notification.studentName);

    // get or create this project
    var projectRecord = getOrCreateRecord("project", studentRecord, notification.projectName);

    // get the class being tested
    var classRecord = getOrCreateRecord("class", projectRecord, notification.className);

    // get the method being tested
    var methodRecord = getOrCreateRecord("method", classRecord, getSignature(notification));

    // get the test itself
    var testMethodRecord = getOrCreateRecord("testMethod", methodRecord, notification.testMethodName + "()");
    testMethodRecord.attr("title", notification.unitTestName + "." + notification.testMethodName + "()");

    // set the width of the testMethod span
    var max = 0;
    $(".testMethod span:first-child").each(function(index, item){
        var width = $(item).width();

        if(width > max) max = width;
    });

    $(".testMethod span:first-child").css("min-width", max + "px");

    // add the result of the test
    var result = resultClosure({
        id: notification.id,
        result: notification.result
    })
    testMethodRecord.find("span:first-child").after(result);

    // add a click handler to the result
    $("#" + notification.id).click(function(){
        $("#attempt").remove();

        if(displayedAttempt !== notification.id) {

            // show this code
            $(this).parent().after(attemptClosure(
                {
                    code: notification.methodSource.length ? notification.methodSource : notification.constructorSource,
                    result: notification.result,
                    exceptionMessage: notification.result !== "success" ? notification.exception.message : null,
                    stackTrace: notification.result !== "success" ? getStackTrace(notification) : null
                }
            ));

            // highlight the sourcecode!
            SyntaxHighlighter.highlight();

            // enable the close button
            $(".close").click(function(){
                $("#attempt").remove();
                displayedAttempt = null;
            });

            // record that this is the attempt open
            displayedAttempt = notification.id;
        } else {
            displayedAttempt = null;
        }

        return false;
    });

}

$(function () {
    connect();
});
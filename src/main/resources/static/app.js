function addRow() {
  var itemId = guid();
  var newRow = $('<tr id="' + itemId + '" class="item">');
  newRow.append('<th scope="row">' + itemId + '</th>');
  newRow.append('<td><i class="fa fa-spinner status"></i></td>');
  newRow.append('<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="deleteRow(\'' + itemId + '\')"><i class="fa fa-trash-alt"></i></button></td>');
  $("#rows").append(newRow);

  checkItems();
}

function deleteRow(itemId) {
  $('#' + itemId).remove();
}

function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
}


var stompClient = null;

function connect(callback) {
  var socket = new SockJS('/ws-check-items');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);
      stompClient.subscribe('/user/queue/results', function (response) {
         JSON.parse(response.body).items.forEach(updateItem);
      });
      callback();
  });
}

function checkItems() {
  if (stompClient === null) {
    connect(checkItems);
  }
  else {
    var items = $.map($(".item"), function(n, i){
      return n.id;
    });

    stompClient.send("/app/checkItems", {}, JSON.stringify({'items': items}));
  }
}

function updateItem(result) {
  var icon = $('#' + result.item).find(".status");
  icon.removeClass();
  if (result.available) {
    icon.addClass('status text-success far fa-check-circle');
  }
  else {
    icon.addClass('status text-danger far fa-times-circle');
  }
}


// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
// Parse.Cloud.define("hello", function(request, response) {
//   response.success("Hello world!");
// });

// Looking for group set to true
Parse.Cloud.afterSave(Parse.User, function(request) {
  var Group = Parse.Object.extend("Group");

  if(request.object.get("lookingForGroup")) {
	var query = new Parse.Query(Group);
	query.notContainedIn('users', [request.object.id]);

	query.find({
		success: function(results) {
			if(!results.length) {
				var group = new Group();
				group.set("users", [request.object.id]);
				group.save();
				return;
			}

			for (var i = 0; i < results.length; i++) { 
				var group = results[i];
				if(group.get('users').length < 4) {
					group.get('users').push(request.object.id);
					group.save();
					break;
				}
			}
		},
		error: function(error) {
			alert("Error: " + error.code + " " + error.message);
		}
	});
  } else {
  	var query = new Parse.Query(Group);
  	query.containedIn('users', [request.object.id]);
	query.find({
		success: function(results) {
			for (var i = 0; i < results.length; i++) { 
				var group = results[i];
				var users = group.get('users');

				for(var j = 0; j < users.length; j++) {
					if(users[j] == request.object.id) {
						users.splice(j, 1);
					}
				}

				group.users = users;
				group.save();
			}
		},
		error: function(error) {
			alert("Error: " + error.code + " " + error.message);
		}
	});
  }
});
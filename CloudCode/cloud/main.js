// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
// Parse.Cloud.define("hello", function(request, response) {
//   response.success("Hello world!");
// });

// Looking for group set to true
Parse.Cloud.afterSave(Parse.User, function(request) {
  if(request.object.get("lookingForGroup")) {
	var Group = Parse.Object.extend("Group");
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
  }
});
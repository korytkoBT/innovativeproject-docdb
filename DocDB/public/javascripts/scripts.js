
			function addAutoComp(newEntry, name){
				var names = ["Mike","Matt","Nancy","Adam","Jenny","Nancy","Carl"];

				var temp = localStorage[name];
				var array = []
				if(temp != null)
					array = temp.split(",");
				array[array.length] = newEntry;
				array = removeDup(array);
				localStorage[name] = array.toString();
			}
			function removeDup(array){
				//O(n^2)
				var uniqueNames = [];
				$.each(array, function(i, el){
					if($.inArray(el, uniqueNames) === -1) uniqueNames.push(el);
				});
				return uniqueNames;

			}
			function changeTag(tagIDName){
				var newTag = document.getElementById(tagIDName).value;
				var	tagID = document.getElementById(tagIDName);
				tagID.value = newTag;
				localStorage[tagIDName] = newTag;
				tag[tagIDName] = newTag+","+$('#geoLoc').html();;
				$(".dz-message span").eq(tagIDName).text(newTag);
				addAutoComp(newTag, 'oldTags');
				//location.reload();
			}

			function checkKey(tagID){
				if (event.keyCode == 13) {
					if (event.keyCode == 13) {
						if(tagID == 'search'){
							addAutoComp(document.getElementById(tagID).value, 'oldSearch');
							return false;
						}
						if(tagID == 'link'){
							handleLink(tagID);
							return false;
						} else {
							changeTag(tagID);
							return false;
						}
					}

				}else
					return false;
			}

			function handleLink(tagIDName){
				var link = document.getElementById(tagIDName).value;
				sendLink(link);
				return false;
			}

			//funtion to flat array of arrays
			var flatten = function (array){
			  var merged = [];
			  merged = merged.concat.apply(merged, array);
			  return merged;
			}
			function getTags(index){
				var temp = document.getElementById('geoLoc').innerHTML;
				var arr = temp.split(",");
				temp = tag[index].replace('#','');
				arr.push(temp);
				return arr;
			}

			function isPic(type){
				if(type == "image/*")
					return true;
				return false;
			}


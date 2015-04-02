/**
* externalized javascript for weekEntry.html.
*
* @author man-at-home
* @since  2014-12-15
*/

/** find all css class names for displayed task input fields. */
function findAllClassesForTasks()
{
	var taskRegEx = /task_\d*$/g;  /* matches task_123 */
	var classes = {};
	
	$('input').each(function() {
		if($(this).attr('class') !== undefined ) {
  
				$($(this).attr('class').split(' ')).each(function() { 
					if (this !== '' && taskRegEx.test(this) ) {
						classes[this] = this;
					}
			});
		}
	});

	return classes;
}

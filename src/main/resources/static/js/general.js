$(document).ready(function() {

	
});

document.querySelectorAll(".nav-link").forEach((link) => {
	link.classList.remove('active');
    if (link.href === window.location.href) {
        link.classList.add("active");
        link.setAttribute("aria-current", "page");
    }
});

function showDiv(flag){
	if(flag) document.getElementById("loader").style.display = 'block';
	else document.getElementById("loader").style.display = 'none';	
}
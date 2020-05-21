function searchStudents() {
    var item = document.getElementById("searchPersons").value;
    let id = document.getElementById("searchPersons").dataset.id;
    if (item.length !== 0 && id != null) {

        window.location.replace("/teacher/classroom/" + id + "?search=" + item);
    }
}

function searchStudentsPresence() {
    var item = document.getElementById("searchPersons").value;
    let id = document.getElementById("searchPersons").dataset.id;
    if (item.length !== 0 && id != null) {
        window.location.replace("/teacher/classroom/" + id + "/presences?search=" + item);
    }
}

function searchStudentsNotes() {
    var item = document.getElementById("searchPersons").value;
    let id = document.getElementById("searchPersons").dataset.id;
    if (item.length !== 0 && id != null) {
        window.location.replace("/teacher/classroom/" + id + "/notes?search=" + item);
    }
}
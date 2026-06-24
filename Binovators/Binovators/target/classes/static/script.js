function load() {
    fetch("/hello")
        .then(res => res.text())
        .then(data => document.getElementById("out").innerText = data);
}



//USER CRUD OPERATIONS
function deleteUser() {
    let id = document.getElementById("userIdD").value;

    fetch("http://localhost:8080/users/" + id, {
        method: "DELETE"
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById("result").innerText = data;
    })
    .catch(error => {
        document.getElementById("result").innerText = "Error deleting user";
    });
}



function updateUser() {

    let id = document.getElementById("userIdU").value;
    let newName = document.getElementById("newName").value;

    fetch("/users/" + id, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: newName
        })
    })
    .then(response => {

        if (!response.ok) {
            throw new Error("User not found");
        }

        return response.json();
    })
    .then(data => {
        document.getElementById("result").innerText =
            "Updated user: "+ data.id + " to " + data.name;
    })
    .catch(error => {
        document.getElementById("result").innerText =
            error.message;
    });

}

//ANIMAL CRUD
function deleteAnimal() {
    let id = document.getElementById("userId").value;

    fetch("http://localhost:8080/animals/" + id, {
        method: "DELETE"
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById("result").innerText = data;
    })
    .catch(error => {
        document.getElementById("result").innerText = "Error deleting animal";
    });
}
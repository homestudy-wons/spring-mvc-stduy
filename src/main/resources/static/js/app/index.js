
const btn = document.getElementById("btn-save");
const title = document.getElementById("title");
const author = document.getElementById("author");
const content = document.getElementById("content");

btn.addEventListener("click", () => {

    const data = {
        title : title.value,
        author : author.value,
        content : content.value
    }

    console.log(data)

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/v1/posts', true);
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(JSON.stringify(data))
    xhr.onload = ()=>{
        if(xhr.status == 200){
            console.log(xhr.response);
            console.log("success");
        }else{
            console.log("fail");
        }
    }
})


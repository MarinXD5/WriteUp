window.onload=function(){
    let button = document.querySelector(".like-btn");
    let icon = document.querySelector(".like_icon");

    button.addEventListener("click", ()=>{
        button.classList.toggle("active");
        if(button.classList.contains("active")){

        }
    });
}

function randomNum(min, max){
    return Math.floor(Math.random()*(max-min+1) + min);
}

function negativePositive(){
    return Math.random() < 0.5 ? -1 : 1;
}

function createClones(button){
    let numberOfClones = randomNum(2,4);

    for (let i=1; i<= numberOfClones; i++){
        let clone = icon.cloneNode(true);
        let size = randomNum(8, 20);
        button.appendChild(clone);
        clone.setAttribute("width", size);
        clone.setAttribute("height", size);
        clone.classList.add("clone");
        clone.style.transform =
            "translate(" +
            negativePositive() * randomNum(15, 30) + "px," +
            negativePositive() * randomNum(15, 30) + "px)";

        let removeNode = setTimeout(() =>{
            button.removeChild(clone);
            clearTimeout(removeNode);
        }, 800);
    }
}
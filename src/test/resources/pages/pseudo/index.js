window.onload = () => {
    const lazyAfterPseudo = document.getElementById("lazy-after-pseudo")
    lazyAfterPseudo.onclick = () => {
        lazyAfterPseudo.classList.add("clicked");
    }

}
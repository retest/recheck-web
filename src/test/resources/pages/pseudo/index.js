

window.onload = () => {
    const change = document.getElementById( "btn-change" );
    change.onclick = () => {
        const target = document.querySelector( change.getAttribute( "data-target" ) );
        if ( target ) {
            if ( target.getAttribute( "data-state" ) ) {
                target.removeAttribute( "data-state" );
            } else {
                target.setAttribute( "data-state", "changed" );
            }
        }
    }
};

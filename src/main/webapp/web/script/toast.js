class Toast {

    static sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    constructor() {
        this.ms = -1;
        let div = this.div = document.createElement("div");
        div.style.position = "fixed";
        div.style.top = "0";
        div.style.left = "0";
        div.style.width = "100%";
        div.style.height = "100%";
        div.style.background = "rgba(0, 0, 0, 0.3";
        {
            let hit = this.hit = document.createElement("div");
            hit.style.borderRadius = "3px";
            hit.style.borderWidth = "0";
            hit.style.borderStyle = "solid";
            hit.style.boxShadow = "0 1px 4px 0 rgba(0, 0, 0, 0.3)";
            hit.style.padding = "8px";
            hit.style.transform = "translate(-50%, -50%)";
            hit.style.position = "absolute";
            hit.style.left = "50%";
            hit.style.top = "50%";
            hit.style.background = "beige";
            hit.style.display = "block";
            div.append(hit);
        }
    }

    div;
    hit;
    ms;

    text(text) {
        this.hit.innerText = text;
        return this;
    }

    show() {
        document.body.append(this.div);
        return this;
    }

    remove() {
        this.div.remove();
        return this;
    }

    async showAndSleep() {
        this.show();
        if (this.ms > 0) {
            await Toast.sleep(this.ms);
            this.remove();
        }
    }

}


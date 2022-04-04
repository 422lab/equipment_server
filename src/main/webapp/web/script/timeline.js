class Timeline {
    /**
     * @param div {HTMLDivElement}
     * @param begin {number}
     * @param end {number}
     */
    constructor(div, begin, end) {
        this.div = div;
        this.begin = begin;
        this.end = end;
        this.inSelect = false;
        let block = div.querySelector(".timeline_select_block");
        if (block != null) {
            block.addEventListener("mousedown", event => this.startEvent(event, event.clientX));
            block.addEventListener("touchstart", event => this.startEvent(event, event.touches[0].clientX));

            block.addEventListener("mousemove", event => this.moveEvent(event, event.clientX));
            block.addEventListener("touchmove", event => this.moveEvent(event, event.touches[0].clientX));

            block.addEventListener("mouseout", event => this.endEvent(event, event.clientX));

            block.addEventListener("mouseup", event => this.endEvent(event, event.clientX));
            block.addEventListener("touchend", event => this.endEvent(event, event.changedTouches[0].clientX));
        }
    }

    startEvent(event, x) {
        if (event.cancelable) {
            event.preventDefault();
        }
        let select = this.div.querySelector(".timeline_select");
        if (select != null) {
            this.selectInto = (x - this.div.offsetLeft) / this.div.clientWidth;
            select.style.left = this.selectInto * 100 + "%";
            select.style.right = (1 - this.selectInto) * 100 + "%";
            this.inSelect = true;
        }
        return false;
    }

    moveEvent(event, x) {
        if (event.cancelable) {
            event.preventDefault();
        }
        if (this.inSelect) {
            let select = this.div.querySelector(".timeline_select");
            if (select != null) {
                let offset = (x - this.div.offsetLeft) / this.div.clientWidth;
                let min = Math.min(offset, this.selectInto);
                let max = Math.max(offset, this.selectInto);
                select.style.left = min * 100 + "%";
                select.style.right = (1 - max) * 100 + "%";
                if (this.onChange != null) {
                    this.onChange(min, max);
                }
            }
        }
        return false;
    }

    endEvent(event, x) {
        if (event.cancelable) {
            event.preventDefault();
        }
        if (this.inSelect) {
            console.log(event.target, event.currentTarget);
            let select = this.div.querySelector(".timeline_select");
            if (select != null) {
                let offset = (x - this.div.offsetLeft) / this.div.clientWidth;
                let min = Math.min(offset, this.selectInto);
                let max = Math.max(offset, this.selectInto);
                select.style.left = min * 100 + "%";
                select.style.right = (1 - max) * 100 + "%";
                this.inSelect = false;
                if (this.onChange != null) {
                    this.onChange(min, max);
                }
            }
        }
        return false;
    }

    /**
     * @type {HTMLDivElement}
     */
    div;
    begin;
    end;
    selectInto;
    inSelect;
    /**
     * @type {function(number, number):void}
     */
    onChange;

    /**
     * @param line {[{start:number, end:number}]}
     */
    load(line) {
        let div = this.div;
        let list = div.querySelector(".timeline_list");
        list.innerHTML = "";
        for (let i = 0; i < line.length; i++) {
            let item = line[i];
            {
                let node = document.createElement("div");
                node.classList.add("timeline_item");
                node.style.left = ((item.start - this.begin) / (this.end - this.begin) * 100) + "%";
                node.style.right = ((this.end - item.end) / (this.end - this.begin) * 100) + "%";
                list.append(node);
            }
        }

    }
}
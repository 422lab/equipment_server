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
        div.addEventListener("mousedown", (event) => {
            let select = div.querySelector(".timeline_select");
            if (select != null) {
                this.selectInto = (event.clientX - div.offsetLeft) / div.clientWidth;
                select.style.left = this.selectInto * 100 + "%";
                select.style.right = (1 - this.selectInto) * 100 + "%";
                this.inSelect = true;
            }
        });
        div.addEventListener("mousemove", (event) => {
            if (this.inSelect) {
                let select = div.querySelector(".timeline_select");
                if (select != null) {
                    let offset = (event.clientX - div.offsetLeft) / div.clientWidth;
                    let min = Math.min(offset, this.selectInto);
                    let max = Math.max(offset, this.selectInto);
                    select.style.left = min * 100 + "%";
                    select.style.right = (1 - max) * 100 + "%";
                    if (this.onChange != null) {
                        this.onChange(min, max);
                    }
                }
            }
        });
        div.addEventListener("mouseup", (event) => {
            if (this.inSelect) {
                let select = div.querySelector(".timeline_select");
                if (select != null) {
                    let offset = (event.clientX - div.offsetLeft) / div.clientWidth;
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
        });
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
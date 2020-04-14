"use strict";module.exports={onBrowser:function(o){var n=o.window;return{name:"marfeel from ".concat(n.document.location.href)}},
    onExtraction:function(o){
        return {html : o.dom.body.innerHTML};
    }
};
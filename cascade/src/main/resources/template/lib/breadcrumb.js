

function BreadCrumb(pageName) {
    var params = queryParameters();

    var links = params.bc ? JSON.parse(atob(params.bc)) : [];

    links.push({name: pageName, url: buildCurrentLink()});

    console.log(links);

    var breadcrumbTemplate = _.template($("#breadcrumb-template").text());
    $("#breadcrumb").append(breadcrumbTemplate({
        links: links
    }));


    this.gotoNewLink = function(url){
        window.location = url + '&bc=' + btoa(JSON.stringify(links));
    };

    this.goto = function(index){
        var url = links[index].url
        var bc = btoa(JSON.stringify(links.slice(0, index)));
        if (url.indexOf('?') > -1){
            url = url + '&bc=' + bc;
        } else {
            url = url + '?bc=' + bc;
        }
        window.location.href = url;
    };


    function buildCurrentLink(){
        var i = window.location.href.indexOf('?');
        if (i == -1){
            i = window.location.href.lastIndexOf('/') + 1;
        } else {
            i = window.location.href.lastIndexOf('/', i) + 1;
        }

        var link = window.location.href.substring(i);

        if (link.indexOf('?') > -1){
            link = link.substring(0,link.indexOf('?'));
        }


        var separator ='?';
        _.each(params, function(value, key){
            if (key !== 'bc') {
                link = link + separator + encodeURI(key) + '=' + encodeURI(value);
                separator = '&';
            }
        });

        return link;
    }
}
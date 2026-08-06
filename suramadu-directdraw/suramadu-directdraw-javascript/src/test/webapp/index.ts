import { DirectDraw } from '../../main/webapp';
import newLocal from './generated/tests.json';

interface ITest {
  name: string;
  originalImg: string[];
  protoImg: string[];
  originalRenderTime: number;
  protoRenderTime: number;
  originalRenderSize: number;
  protoRenderSize: number;
}

export class DirectDrawTestControl {
  constructor(private data: ITest[]) {}

  public start() {
    const selected = getUrlParameter('n');

    this.data.forEach((method, index) => {
      if (selected == null || (selected != null && index === selected)) {
        document.body.innerHTML += '<div id="' + method.name + 'label"></div>';
        document.body.innerHTML +=
          '<canvas id="' +
          method.name +
          'DD" style="width:500px;height:100px"></canvas>';
        document.body.innerHTML += '<img id="' + method.name + 'PNG" />';
      }
    });
    const dpr = Math.ceil(window.devicePixelRatio) || 1;
    console.warn('DPR=' + dpr);
    let sequence1 = Promise.resolve();
    this.data.forEach((method, index) => {
      sequence1 = sequence1.then(() => {
        executeMethod(method, index);
      });
    });

    function executeMethod(test: ITest, index: number) {
      const dd = new DirectDraw({ dpr, logTrace: true });
      return new Promise(resolve => {
        if (selected == null || (selected != null && index === selected)) {
          const canvasDD = document.getElementById(
            test.name + 'DD'
          ) as HTMLCanvasElement;
          canvasDD.width = 500 * dpr;
          canvasDD.height = 100 * dpr;
          const ctxDD = canvasDD.getContext('2d')!;
          addInfo(test, index, document.getElementById(test.name + 'label')!);
          let sequence = Promise.resolve();
          test.protoImg.forEach(img => {
            sequence = sequence
              .then(() => {
                console.warn('started ' + test.name);
                return dd.draw64(img);
              })
              .then(resultCanvas => {
                console.warn('finished ' + test.name);
                ctxDD.drawImage(resultCanvas, 0, 0);
              })
              .catch(error => {
                console.error(error.message, error.stack);
              });
          });
          sequence.then(() => {
            resolve();
          });
          test.originalImg.forEach(img => {
            drawImage(test.name, img);
          });
        } else {
          resolve();
        }
      });
    }
  }
}

function addInfo(test: ITest, index: number, element: HTMLElement) {
  element.innerHTML =
    index +
    '. ' +
    ((test.protoRenderSize / test.originalRenderSize) * 100).toPrecision(4) +
    '% in size, ' +
    ((test.protoRenderTime / test.originalRenderTime) * 100).toPrecision(4) +
    '% in time';
}

function drawImage(method: string, b64image: string) {
  const imageObj = document.getElementById(method + 'PNG') as HTMLImageElement;
  // imageObj.onload = function () {
  //     let context = canvas.getContext("2d");
  //     context.drawImage(imageObj, 500, 0);
  //     imageObj.onload = null;
  //     imageObj.src = '';
  // };
  imageObj.src = 'data:image/png;base64,' + b64image;
}

function getUrlParameter(sParam: string) {
  const sPageURL = window.location.search.substring(1);
  const sURLletiables = sPageURL.split('&');
  for (const urlvars of sURLletiables) {
    const sParameterName = urlvars.split('=');
    if (sParameterName[0] === sParam) {
      return +sParameterName[1];
    }
  }
  return null;
}

new DirectDrawTestControl(newLocal).start();
